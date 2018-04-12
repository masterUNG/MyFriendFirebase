package masterung.androidthai.in.th.myfriendfirebase.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import masterung.androidthai.in.th.myfriendfirebase.MainActivity;
import masterung.androidthai.in.th.myfriendfirebase.R;
import masterung.androidthai.in.th.myfriendfirebase.ServiceActivity;
import masterung.androidthai.in.th.myfriendfirebase.utility.MyAlert;
import masterung.androidthai.in.th.myfriendfirebase.utility.UserModel;

public class RegisterFragment extends Fragment {

    //    Explicit
    private ImageView imageView;
    private Uri uri;
    private String nameString, emailString, passwordString, photoURLString;
    private boolean aBoolean = true;
    private ProgressDialog progressDialog;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Create Toolbar
        createToolbar();

//        Image Controller
        imageController();

    }   // Main Method

    private void createToolbar() {

        setHasOptionsMenu(true);

        Toolbar toolbar = getView().findViewById(R.id.toolbarRegister);
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);

        ((MainActivity) getActivity()).getSupportActionBar()
                .setTitle(getString(R.string.register_to_firebase));
        ((MainActivity) getActivity()).getSupportActionBar()
                .setSubtitle("Choose Image & Fill All Blank");

        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itemUpload) {
            uploadToFirebase();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void uploadToFirebase() {

        MyAlert myAlert = new MyAlert(getActivity());

        EditText nameEditText = getView().findViewById(R.id.editTextDisplayName);
        EditText emailEditText = getView().findViewById(R.id.editTextEmail);
        EditText passwordEditText = getView().findViewById(R.id.editTextPassword);

        nameString = nameEditText.getText().toString().trim();
        emailString = emailEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please Wait ...");
        progressDialog.setMessage("Process Continue wait few Minus");
        progressDialog.show();

        if (aBoolean) {
            myAlert.myNormalDialog("Choose Image ?",
                    "Please Choose Image Avata");
            progressDialog.dismiss();
        } else if (nameString.isEmpty() || emailString.isEmpty() || passwordString.isEmpty()) {
            myAlert.myNormalDialog(getString(R.string.title_have_space),
                    getString(R.string.message_have_space));
            progressDialog.dismiss();
        } else {

//            upload Image To Firebase Storage
            uploadImage();

        }   //if

    }   // uploadToFirebase

    private void uploadImage() {

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference()
                .child("Avata/" + nameString);

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("3AprilV1", "Upload Image Success");

                findPhotoURLonFirebase();

                registerNewUserToFirebase();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("3AprilV1", "Error --> " + e.toString());
            }
        });

    }

    private void findPhotoURLonFirebase() {

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("Avata/" + nameString)
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        photoURLString = uri.toString();
                        Log.d("9AprilV1", "photoURL ==> " + photoURLString);
                    }
                });

    }

    

    private void registerNewUserToFirebase() {

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(getActivity(), "Register Success",
                                    Toast.LENGTH_SHORT).show();

//                            Setup DisplayName
                            UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
                            builder.setDisplayName(nameString);
                            UserProfileChangeRequest userProfileChangeRequest = builder.build();

                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            firebaseUser.updateProfile(userProfileChangeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("8AprilV1", "Update DisplayName Success");

//                                    Update Value to Database Firebase
                                    updateValueToDatabaseFirebase();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("8AprilV1", "Cannot ==> " + e.toString());
                                }
                            });


                            startActivity(new Intent(getActivity(), ServiceActivity.class));
                            getActivity().finish();

                        } else {

                            MyAlert myAlert = new MyAlert(getActivity());
                            myAlert.myNormalDialog("Cannot Register",
                                    task.getException().getMessage());

                        }

                        progressDialog.dismiss();

                    }
                });
    }

    private void updateValueToDatabaseFirebase() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String uidUserString = firebaseUser.getUid();

//        Check Value
        String tag = "10AprilV1";
        Log.d(tag, "Email ==> " + emailString);
        Log.d(tag, "DisplayName ==> " + nameString);
        Log.d(tag, "PhotoUrl ==> " + photoURLString);
        Log.d(tag, "UidUser ==> " + uidUserString);

//        Setter to Model
        UserModel userModel = new UserModel(emailString, nameString,
                photoURLString, uidUserString);

//        Connected Database Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference()
                .child("User")
                .child(uidUserString);

        databaseReference.setValue(userModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Cannot Register ==> " + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_register, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {

            aBoolean = false;

//            Success Choose Image
            try {

                uri = data.getData();
                Bitmap bitmap = BitmapFactory
                        .decodeStream(getActivity().getContentResolver().openInputStream(uri));
                imageView.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }   // if
    }   // onActivityResult

    private void imageController() {
        imageView = getView().findViewById(R.id.imageViewAvata);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Please Choose Image"),
                        1);

            }   // onClick
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }
}
