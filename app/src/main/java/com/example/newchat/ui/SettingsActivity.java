package com.example.newchat.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.example.newchat.R;
import com.example.newchat.database.UsersDao;
import com.example.newchat.database.model.User;
import com.example.newchat.util.DataUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView mImageSettings;
    private EditText mUserNameSet, mProfileStatusSet;
    private Button mSettingsButtonUpdate;
    private FirebaseAuth auth;
    private MaterialDialog materialDialog;
    private int Request_Camera = 100, Select_Image = 101;

    private Uri imageUri;
    private ProgressDialog progressDialog;
    private StorageReference mImageStorage;
    String image;
    private EditText mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        progressDialog = new ProgressDialog(this);

        initView();
        getUserOnce();
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mImageStorage = FirebaseStorage.getInstance().getReference();


    }

    private void initView() {
        mImageSettings = findViewById(R.id.settings_image);
        mImageSettings.setOnClickListener(this);
        mUserNameSet = findViewById(R.id.set_user_name);
        mProfileStatusSet = findViewById(R.id.set_profile_status);
        mSettingsButtonUpdate = findViewById(R.id.update_settings_button);
        mSettingsButtonUpdate.setOnClickListener(this);
        mPhone = (EditText) findViewById(R.id.phone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_image:
                selectImage();
                break;
            case R.id.update_settings_button:
                String name = mUserNameSet.getText().toString().trim();
                String status = mProfileStatusSet.getText().toString().trim();
                String phone =mPhone.getText().toString().trim();
                if (isValidForm(name, status , phone))
                    updateUser(name, status, image , phone);
                break;
            default:
                break;
        }
    }

    private boolean isValidForm( String name, String status , String phone) {
        boolean isValid = true;
        if (name.isEmpty()) {
            mUserNameSet.setError("Required");
            isValid = false;
        } else {
            mUserNameSet.setError(null);
            isValid = true;
        }
        if (status.isEmpty()) {
            mProfileStatusSet.setError("Required");
            isValid = false;
        } else {
            mProfileStatusSet.setError(null);
            isValid = true;
        }
        if (phone.isEmpty()) {
            mPhone.setError("Required");
            isValid = false;
        } else {
            mPhone.setError(null);
            isValid = true;
        }
        return isValid;

    }

    private void updateUser( String name, String status, String img , String phone) {
        auth = FirebaseAuth.getInstance();
        UsersDao.updateUser(auth.getCurrentUser().getUid(), name, phone, image, status, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(SettingsActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                }

            }
        });


    }

    private void getUserOnce() {
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            UsersDao.getCurrentUser(auth.getCurrentUser().getUid(), new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataUtil.currentUser = task.getResult().toObject(User.class);

                        mUserNameSet.setText(task.getResult().getString("name"));
                        mProfileStatusSet.setText(task.getResult().getString("status"));
                        mPhone.setText(task.getResult().getString("phone"));
                        image = task.getResult().getString("image");
                        Glide.with(SettingsActivity.this)
                                .load(image)
                                .into(mImageSettings);

                    }
                }
            });
        }
    }

    private void selectImage() {
        materialDialog = new MaterialDialog.Builder(this)
                .title(R.string.uploadImages)
                .items(R.array.uploadImages)
                .itemsIds(R.array.itemIds)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:

                                Intent intentgallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intentgallery.setType("image/*");
                                if (intentgallery.resolveActivity(getPackageManager()) != null)
                                    startActivityForResult(intentgallery.createChooser(intentgallery, "Select File"), Select_Image);
                                break;
                            case 1:
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (intent.resolveActivity(getPackageManager()) != null)
                                    startActivityForResult(intent, Request_Camera);
                                break;
                            case 2:
                                materialDialog.dismiss();
                                break;
                        }
                    }
                })
                .show();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        auth = FirebaseAuth.getInstance();

        if (requestCode == Select_Image && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(this);
        }
        if (requestCode == Request_Camera && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
           Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (bitmap != null) {
                //memoryImage.setImageBitmap(bitmap);
            }

            save(imageUri);


        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                save(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error + "", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
    super.onActivityResult(requestCode, resultCode, data);

    if (resultCode == Activity.RESULT_OK) {

        if (requestCode == Request_Camera) {

            bitmap = (Bitmap) data.getExtras().get("data");
            if (bitmap != null) {
                //memoryImage.setImageBitmap(bitmap);
            }

        } else if (requestCode == Select_Image) {
           if  (data != null) {
                try {
                    final Uri SelectedImageUri = data.getData();
                    final InputStream stream = getContentResolver().openInputStream(SelectedImageUri);
                    bitmap = BitmapFactory.decodeStream(stream);
                    CropImage.activity(SelectedImageUri)
                            .setAspectRatio(1, 1)
                            .setMinCropWindowSize(500, 500)
                            .start(this);
                  //  memoryImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
     */
    public void save(Uri resultUri) {
        final StorageReference filepath = mImageStorage.child("profile_images" + auth.getCurrentUser().getUid());
        filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        image = uri.toString();
                        Glide.with(SettingsActivity.this)
                                .load(image)
                                .into(mImageSettings);
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "aa", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double prog = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("uploading" + (int) prog + "%");
                progressDialog.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SettingsActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

