package desahogate.proyectofinal.desahogate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegistroActivity extends AppCompatActivity {

    //Conexión a la BBDD
    private DatabaseReference mDatabase;

    //ProgressDialog
    private ProgressDialog mRegProgress;

    //Firebase Auth
    private FirebaseAuth mAuth;

    //Inicializar
    private TextInputLayout mDisplayname;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private TextInputLayout mPasswordValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        inicializar();
    }

    private void inicializar() {
        mAuth = FirebaseAuth.getInstance();
        mRegProgress = new ProgressDialog(this);

        mDisplayname = (TextInputLayout) findViewById(R.id.userName);
        mEmail = (TextInputLayout) findViewById(R.id.email);
        mPassword = (TextInputLayout) findViewById(R.id.password);
        mPasswordValid = (TextInputLayout) findViewById(R.id.passwordVerificar);

    }

    public void registrarUsuario(View view) {
        View focusView = null;
        boolean cancel = false;

        // Reset errors.
        mDisplayname.setError(null);
        mEmail.setError(null);
        mPassword.setError(null);
        mPasswordValid.setError(null);

        //Obtenemos el nombre de usuario, email y la contraseña desde las cajas de texto
        String display_name = mDisplayname.getEditText().getText().toString().trim();
        String email = mEmail.getEditText().getText().toString().trim();
        String password = mPassword.getEditText().getText().toString().trim();
        String passwordValid = mPasswordValid.getEditText().getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacías
        if (TextUtils.isEmpty(display_name)) {
            mDisplayname.setError(getString(R.string.error_field_required));
            focusView = mDisplayname;
            cancel = true;
            Toast.makeText(this, "Debe ingresar nombre de usuario", Toast.LENGTH_LONG).show();
//            return;
        } else if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.error_field_required));
            focusView = mEmail;
            cancel = true;
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
//            return;
        } else if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.error_field_required));
            focusView = mPassword;
            cancel = true;
            Toast.makeText(this, "Falta ingresar la contraseña", Toast.LENGTH_LONG).show();
//            return;
        } else if (TextUtils.isEmpty(passwordValid)) {
            mPasswordValid.setError(getString(R.string.error_field_required));
            focusView = mPasswordValid;
            cancel = true;
            Toast.makeText(this, "Falta ingresar la contraseña", Toast.LENGTH_LONG).show();
//            return;
        }


        if (!isEmailValid(email)) {
            mEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEmail;
            cancel = true;
//            return;
        } else if (!isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
//            return;
        } else if (!ismPasswordValid(password, passwordValid)) {
            mPasswordValid.setError(getString(R.string.error_invalid_passwordValid));
            focusView = mPasswordValid;
            cancel = true;
//            return;
        }

        if (cancel) {
            // Hubo un error; no intente iniciar sesión y enfoque el primer
            // campo de formulario con un error.
            focusView.requestFocus();
//            return;
        } else {
            mRegProgress.setTitle("Registrando Usuario");
            mRegProgress.setMessage("Por favor espere mientras creamos su cuenta!");
            mRegProgress.setCanceledOnTouchOutside(false);
            mRegProgress.show();

            register_user(display_name, email, password);
        }

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        boolean contener = false;
        if (email.contains("@") && email.contains("."))
            contener = true;

        return contener;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

    private boolean ismPasswordValid(String password, String pwValidar) {
        //TODO: Replace this with your own logic
        boolean valida = false;
        if (password.equals(pwValidar))
            valida = true;

        return valida;
    }

    /**
     * Registro de Usuarios
     *
     * @param display_name
     * @param email
     * @param password
     */
    private void register_user(final String display_name, final String email, final String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();//Identificador de cada Usuario

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", display_name);
                    userMap.put("status", "Disponible");
                    userMap.put("biography", "Escriba su biografía");
                    userMap.put("image", "default");
                    userMap.put("email", email);

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                mRegProgress.dismiss();

                                Intent mainIntent = new Intent(RegistroActivity.this, LoginActivity.class);
                                startActivity(mainIntent);
                                finish();

                            }
                        }
                    });


                } else {

                    mRegProgress.hide();
                    Toast.makeText(RegistroActivity.this, "No se puede registrar. Verifique el formulario e intente de nuevo.", Toast.LENGTH_LONG).show();

                }

            }
        });

    }
}
