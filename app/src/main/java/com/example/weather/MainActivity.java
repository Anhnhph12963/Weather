package com.example.weather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.Model.Comments;
import com.example.weather.Model.Posts;
import com.example.weather.adapter.MyViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navView;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference, databasePost, databaseLike, databaseComment;
    String ImageUrlV, usernameV;
    CircleImageView circleImageView;
    TextView tv_username, tv_title;
    ImageView img_post, img_send, img_back;
    EditText edt_post;
    private static final int REQUEST_CODE = 101;
    Uri imageUri;
    ProgressDialog progressDialog;
    StorageReference storageReference;
    FirebaseRecyclerAdapter<Posts, MyViewHolder> adapter;
    FirebaseRecyclerOptions<Posts> options;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Comments> CommentOptions;
    FirebaseRecyclerAdapter<Comments, CommentViewHolder> CommentViewHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("App weather");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        // thêm

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        databasePost = FirebaseDatabase.getInstance().getReference().child("Post");
        databaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        databaseComment = FirebaseDatabase.getInstance().getReference("Comment");
        storageReference = FirebaseStorage.getInstance().getReference().child("PostImage");
        Log.d("TAG", "onCreate: " + firebaseUser.getUid());

        img_post = findViewById(R.id.img_post);
        edt_post = findViewById(R.id.edt_post);
        img_send = findViewById(R.id.img_send);
        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);

        img_back = findViewById(R.id.img_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setVisibility(View.GONE);
        img_back.setVisibility(View.GONE);

        View view = navView.inflateHeaderView(R.layout.drawer_header);
        circleImageView = view.findViewById(R.id.profileimage);
        tv_username = view.findViewById(R.id.tv_username);

        navView.setNavigationItemSelectedListener(this);
        progressDialog = new ProgressDialog(this);
        recyclerView = findViewById(R.id.rycView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPost();
            }
        });

        img_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        LoadPost();

    }

    private void LoadPost() {
        options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(databasePost, Posts.class).build();
        adapter = new FirebaseRecyclerAdapter<Posts, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Posts model) {
                final String postKey = getRef(position).getKey();
                holder.title_post.setText(model.getPostTitle());
                String timeAgo = caculateTimeAgo(model.getDatePost());
                holder.tv_timeAge.setText(timeAgo);
                holder.tv_username.setText(model.getUserName());

                Picasso.get().load(model.getPostImage()).into(holder.img_post);
                Picasso.get().load(model.getUserProfile()).into(holder.profile_image);
                holder.countLike(postKey, firebaseUser.getUid(), databaseLike);

                holder.img_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseLike.child(postKey).child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    databaseLike.child(postKey).child(firebaseUser.getUid()).removeValue();
                                    holder.img_like.setColorFilter(Color.GRAY);
                                    notifyDataSetChanged();
                                } else {
                                    databaseLike.child(postKey).child(firebaseUser.getUid()).setValue("like");
                                    holder.img_like.setColorFilter(Color.BLUE);
                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                holder.img_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String comment = holder.edt_comment.getText().toString();
                        if (comment.isEmpty()) {
                            Toast.makeText(MainActivity.this, "vui long nhap du lieu", Toast.LENGTH_SHORT).show();
                        } else {
                            AddComment(holder, postKey, databaseComment, firebaseUser.getUid(), comment);
                        }
                    }
                });
                LoadComment(postKey);
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_post, parent, false);
                return new MyViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void LoadComment(String postKey) {
        MyViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        CommentOptions = new FirebaseRecyclerOptions.Builder<Comments>().setQuery(databaseComment.child(postKey), Comments.class).build();
        CommentViewHolder = new FirebaseRecyclerAdapter<Comments, com.example.weather.CommentViewHolder>(CommentOptions) {
            @Override
            protected void onBindViewHolder(@NonNull com.example.weather.CommentViewHolder holder, int position, @NonNull Comments model) {

                Picasso.get().load(model.getProfileImage()).into(holder.img_user_comment);
                holder.tv_username.setText(model.getUsername());
                holder.tv_comment.setText(model.getComment());
            }

            @NonNull
            @Override
            public com.example.weather.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_comment, parent, false);
                return new CommentViewHolder(view);
            }
        };
        CommentViewHolder.startListening();
        MyViewHolder.recyclerView.setAdapter(CommentViewHolder);
    }

    private void AddComment(MyViewHolder holder, String postKey, DatabaseReference databaseComment, String uid, String comment) {
        HashMap hashMap = new HashMap();
        hashMap.put("username", usernameV);
        hashMap.put("profileImage", ImageUrlV);
        hashMap.put("comment", comment);

        databaseComment.child(postKey).child(uid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "comment thanh cong", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    holder.edt_comment.setText(null);
                } else {
                    Toast.makeText(MainActivity.this, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String caculateTimeAgo(String datePost) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy hh:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            long time = sdf.parse(datePost).getTime();
            long now = System.currentTimeMillis();
            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);

            return ago + "";
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            img_post.setImageURI(imageUri);
        }
    }

    private void AddPost() {
        final String PostTitle = edt_post.getText().toString();
        if (PostTitle.isEmpty() || PostTitle.length() < 1) {
            edt_post.setError("không hợp lệ");
        } else if (imageUri == null) {
            Toast.makeText(this, "Vui lòng chọn hình ảnh", Toast.LENGTH_SHORT).show();
        } else {

            progressDialog.setTitle("Đang tải lên");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyy hh:mm:ss");
            String strDate = simpleDateFormat.format(date);

            storageReference.child(firebaseUser.getUid() + strDate).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        storageReference.child(firebaseUser.getUid() + strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                HashMap hashMap = new HashMap();
                                hashMap.put("datePost", strDate);
                                hashMap.put("postImage", uri.toString());
                                hashMap.put("postTitle", PostTitle);
                                hashMap.put("userProfile", ImageUrlV);
                                hashMap.put("userName", usernameV);

                                databasePost.child(firebaseUser.getUid() + strDate).setValue(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "Đăng lên thành công", Toast.LENGTH_SHORT).show();
                                            img_post.setImageResource(R.drawable.ic_image);
                                            edt_post.setText("");
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseUser == null) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        } else {
            databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        ImageUrlV = snapshot.child("profileImage").getValue().toString();
                        usernameV = snapshot.child("username").getValue().toString();
                        Picasso.get().load(ImageUrlV).into(circleImageView);
                        tv_username.setText(usernameV);
                        Log.d("TAG", "onDataChange: " + firebaseUser.getUid());
                        Log.d("TAG", "onDataChange: " + ImageUrlV);
                        Log.d("TAG", "onDataChange: " + usernameV);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.home) {
            //   Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
        } else if (menuItem.getItemId() == R.id.profile) {
            //   Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
        } else if (menuItem.getItemId() == R.id.maps) {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
            //  Toast.makeText(this, "Google maps", Toast.LENGTH_SHORT).show();
        } else if (menuItem.getItemId() == R.id.findfriend) {
            //     Toast.makeText(this, "find friend", Toast.LENGTH_SHORT).show();
        } else if (menuItem.getItemId() == R.id.friend) {
            //   Toast.makeText(this, "friend", Toast.LENGTH_SHORT).show();
        } else if (menuItem.getItemId() == R.id.chat) {
//            Toast.makeText(this, "chat", Toast.LENGTH_SHORT).show();
        } else if (menuItem.getItemId() == R.id.logout) {
            //   Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            firebaseAuth.signOut();
            finish();
        } else {
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return true;
    }
}