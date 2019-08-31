package com.example.user.policeassistant;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    StorageReference storageRef;
    StorageReference Dref;
    static StorageReference sr;
    private FloatingActionButton fabNavigation;
    private DatabaseReference mdatabase;
    private String SplitUsername;
    private Button save;
    DatabaseReference PostID;
    public int postid;

    private RecyclerView mBloglist;
    private DatabaseReference mDatabase;
    private static Context context;

    public String ur;

    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;

    NavigationView navigationView;
    String Usernameprofile;
    View header;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        fab = findViewById(R.id.fabbutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //some works
            }
        });

        bottomAppBar = findViewById(R.id.bottomnavbar);
        bottomAppBar.replaceMenu(R.menu.bottomnavmenu);
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.messages:
                        //some works
                        break;
                    case R.id.calls:
                        //some works
                        break;
                    case R.id.notifications:
                        //some works
                        break;
                }
                return true;
            }
        });


        Dref=FirebaseStorage.getInstance().getReference("postImage");
        sr=FirebaseStorage.getInstance().getReference("PostImage");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();

        String[] part =email.split("@");
        SplitUsername=part[0];

        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mdatabase=FirebaseDatabase.getInstance().getReference("Users");
        storageRef= FirebaseStorage.getInstance().getReference();
        save=findViewById(R.id.savePost);
        PostID=FirebaseDatabase.getInstance().getReference();



        mdatabase.child(SplitUsername).child("Username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usernameprofile=dataSnapshot.getValue().toString();
                setUsername(Usernameprofile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       // View hg = getLayoutInflater().inflate(R.layout.activity_homepage_general, null);
        mDatabase= FirebaseDatabase.getInstance().getReference("Posts");
        mDatabase.keepSynced(true);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);

        mBloglist=findViewById(R.id.recyclerId);
        mBloglist.setHasFixedSize(true);
        mBloglist.setLayoutManager(mLinearLayoutManager);

        final ImageView im=findViewById(R.id.postImage);

        context = this;


        drawerLayout= findViewById(R.id.drawerlayout);
        navigationView= findViewById(R.id.navigationview);

        navigationView.setNavigationItemSelectedListener(this);
        fabNavigation=findViewById(R.id.fabbutton);
        header=navigationView.getHeaderView(0);

        TextView profilemail=header.findViewById(R.id.profileEmail);

        final CircleImageView proImage=header.findViewById(R.id.profileImage);

        profilemail.setText(email);



        storageRef.child("ProfilePicture").child(SplitUsername).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                ur= uri.toString();
                Glide.with(getApplicationContext()).load(ur).into(proImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });




        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        fabNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PostID.child("Posts").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      // postid= (int) dataSnapshot.getChildrenCount();
                        //Toast.makeText(getApplicationContext(),String.valueOf(postid),Toast.LENGTH_SHORT).show();
                       /* if(dataSnapshot!=null)
                        {
                            postid= (int) dataSnapshot.getChildrenCount();
                        }*/
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent intent=new Intent(getApplicationContext(),Post.class);
                startActivity(intent);
                finish();
               // String id=T.getText().toString();
               //Toast.makeText(getApplicationContext(),String.valueOf(postid),Toast.LENGTH_SHORT).show();



            }
        });


    }



    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<Blog,NavigationActivity.BlogViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Blog, NavigationActivity.BlogViewHolder>
                (Blog.class,R.layout.activity_list_item_row, NavigationActivity.BlogViewHolder.class,mDatabase){

            @Override
            protected void populateViewHolder(NavigationActivity.BlogViewHolder viewHolder, final Blog model, final int position) {
                try{
                viewHolder.setTitle(model.getTitle());
                viewHolder.setCriminalsName(model.getCriminalsName());
                viewHolder.setFathersName(model.getFathersName());
                viewHolder.setPresentAdd(model.getPresentAdd());
                viewHolder.setRewards(model.getRewards());
                String st=model.getTitle();
                viewHolder.setImage(st);
                final int p=viewHolder.getPosition();
                final DatabaseReference rf=getRef(p);
                viewHolder.SavePost(rf);






                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent=new Intent(context,PostExpand.class);

                        TextView fullname=findViewById(R.id.fullname);
                        TextView fathersName=findViewById(R.id.fatherfullname);
                        TextView district=findViewById(R.id.districtname);
                        TextView rewards=findViewById(R.id.points);
                        ImageView imageView=findViewById(R.id.postImage);

                        Pair[] pairs=new Pair[4];
                        pairs[0]=new Pair<View,String>(fullname,"Name");
                        pairs[1]=new Pair<View,String>(fathersName,"FatherName");
                        pairs[2]=new Pair<View,String>(district,"District");
                        pairs[3]=new Pair<View,String>(rewards,"Rewards");





                        ActivityOptionsCompat compat= ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,imageView,"pic");
                        intent.putExtra("name",model.getCriminalsName());
                        intent.putExtra("father",model.getFathersName());
                        intent.putExtra("PresentAddress",model.getPresentAdd());
                        intent.putExtra("Description",model.getDescription());
                        intent.putExtra("mother",model.getMothersName());
                        intent.putExtra("PermanentAddress",model.getPermanentAdd());
                        intent.putExtra("rewards",model.getRewards());
                        intent.putExtra("Title",model.getTitle());
                        context.startActivity(intent,compat.toBundle());

//                        Pair[] pairs=new Pair[2];
//                        pairs[0]=new Pair<View,String>(imageView,"listImageTransition");
//
//                        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(NavigationActivity.this, pairs);
                        //context.startActivity(intent);
                    }
                });

            }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Post showing Error or No post to Show!",Toast.LENGTH_SHORT).show();
                }
            }

        };

        mBloglist.setAdapter(firebaseRecyclerAdapter);

    }


    public static class BlogViewHolder extends RecyclerView.ViewHolder
    {

        View mview;
        public BlogViewHolder(final View itemView)
        {
            super(itemView);
            mview=itemView;

        }

        public void setTitle(String title)
        {
            TextView post_Title=mview.findViewById(R.id.post_title);
            post_Title.setText(title);
        }

        public void setPresentAdd(String dist)
        {
            TextView post_dist=mview.findViewById(R.id.districtname);
            post_dist.setText(dist);
        }
        public void setCriminalsName(String criminalsName)
        {
            TextView post_name=mview.findViewById(R.id.fullname);
            post_name.setText(criminalsName);
        }


        public void setFathersName(String father)
        {
            TextView post_father=mview.findViewById(R.id.fatherfullname);
            post_father.setText(father);
        }


        public void setRewards(String rewards)
        {
            TextView post_reward=mview.findViewById(R.id.points);
            post_reward.setText(rewards);
        }

        public void setImage(String s)
        {
            final ImageView imageView=mview.findViewById(R.id.postImage);

            try{


                if(s.isEmpty())
                {

                    s="NO_IMAGE.jpg";
                }

            sr.child(s).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                  String url=uri.toString();
                  Glide
                          .with(context)
                          .load(url)
                          .apply(new RequestOptions().override(510, 523))
                         .into(imageView);
                }
            });}catch (Exception e)
            {
                Toast.makeText(context,"Error getting image",Toast.LENGTH_SHORT).show();
            }


        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            String[] parts = user.getEmail().toString().split("@");
            final String mail = parts[0];

        private void copyRecord(DatabaseReference fromPath, final DatabaseReference toPath) {

            fromPath.addListenerForSingleValueEvent(new ValueEventListener()  {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    toPath.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(context,"Post Saved Successfully",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(context,"Post Saving Failed",Toast.LENGTH_SHORT).show();
                }
            });
        }




        public void SavePost(final DatabaseReference fromPath)
        {
            Button savepost=mview.findViewById(R.id.savePost);
            savepost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseReference ToPath=FirebaseDatabase.getInstance().getReference("User Posts").child(mail).child("SavedPost").push();
                    copyRecord(fromPath,ToPath);
                }
            });
        }



    }


    public void setUsername(String user)
    {
        TextView textviewUser=header.findViewById(R.id.profileUsername);
        textviewUser.setText(user);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        switch (id){
            case R.id.contact:
                //some work..............................
                break;
            case R.id.rateus:
                //some work.............................
                break;
            case R.id.search:
                //some work.............................
                break;
            case android.R.id.home:
                //some work...........................
                finish();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id=menuItem.getItemId();
        switch (id){
            case R.id.profile:
                //some work..............................
                Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(profile);
                finish();
                break;
            case R.id.savedposts:
                //some work.............................

                //DatabaseReference savedRef=FirebaseDatabase.getInstance().getReference("User Posts").child(mail).child("SavedPost");
               /* Intent savedposts=new Intent(getApplicationContext(),SavedPostsActivity.class);
                startActivity(savedposts);
                finish();*/

                break;
            case R.id.rewards:
                //some work.............................
                /*Intent intent=new Intent(getApplicationContext(),Recycle.class);
                startActivity(intent);
                finish();*/

                break;
            case R.id.settings:
                //some work...........................
                break;
            case R.id.logout:
                //some work..............................
                FirebaseAuth.getInstance().signOut();
                Intent logout = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(logout);
                finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }
}
