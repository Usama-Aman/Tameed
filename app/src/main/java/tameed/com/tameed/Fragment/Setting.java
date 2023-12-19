package tameed.com.tameed.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tameed.com.tameed.About;
import tameed.com.tameed.Adapter.helper;
import tameed.com.tameed.Contact_us;
import tameed.com.tameed.MainActivity;
import tameed.com.tameed.My_account;
import tameed.com.tameed.My_services;
import tameed.com.tameed.Payment_type;
import tameed.com.tameed.R;
import tameed.com.tameed.RegisterActivity;
import tameed.com.tameed.ResentChat;
import tameed.com.tameed.Terms_Condition;
import tameed.com.tameed.Util.Apis;
import tameed.com.tameed.Util.AppController;
import tameed.com.tameed.Util.LoadApi;
import tameed.com.tameed.Util.SaveSharedPrefernces;

import static android.app.Activity.RESULT_OK;
import static tameed.com.tameed.MapView_Activity.RequestPermissionCode;

/**
 * Created by dev on 16-01-2018.
 */

public class Setting extends Fragment {
    private static int LOAD_IMAGE_RESULTS = 1000;
    private final int SHOW_PROG_DIALOG = 0, HIDE_PROG_DIALOG = 1, LOAD_QUESTION_SUCCESS = 2;
    ConstraintLayout payment_report, profile, notificaton, chat, share_app, my_service, terms_condition, about_tameed, contact_us, my_acount, logout;
    ImageView user_profile_img, notification_img;
    Bitmap bitmap;
    ImageView imageView37;
    Uri imageUri;
    byte[] picture = null;
    ProgressBar progress;
    String pic_url, pic_thumb_url, profilepic, pic_thumb, image_url, notify, notify_update, notify_save;
    JSONObject jobj;
    Boolean bool = false;
    String action = "Uploadprofilepic", user_id, msg = "";
    SaveSharedPrefernces ssp;
    private ProgressDialog progress_dialog;
    Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case SHOW_PROG_DIALOG:
                    showProgDialog();
                    break;

                case HIDE_PROG_DIALOG:
                    hideProgDialog();
                    break;

                case LOAD_QUESTION_SUCCESS:

                    break;

                default:
                    break;
            }

            return false;
        }

    });
    private TextView setting_name, seting_email;
    private String TAG = "Setting";
    private TextView badge_txt_setting_chat;
    private AsyncLoadData asyncLoad;
    ConstraintLayout constraintLayout5, constraintFavourite;

    //ADD JAY
    private String tag_string_req = "string_req";

    public static Bitmap getBitmapFromURL(String src) {
        try {

            ////Log.e("srcsrcsrc...", "..." + src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        String languageToLoad = "ar"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getResources().updateConfiguration(config,
                getActivity().getResources().getDisplayMetrics());
        View rootView = inflater.inflate(R.layout.s, container, false);
        ssp = new SaveSharedPrefernces();
        user_id = ssp.getUserId(getActivity());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        badge_txt_setting_chat = (TextView) rootView.findViewById(R.id.badge_txt_setting_chat);


        progress = rootView.findViewById(R.id.progress);
        ////Log.e(TAG, "****************************");
        chat = (ConstraintLayout) rootView.findViewById(R.id.con_chat);
        payment_report = (ConstraintLayout) rootView.findViewById(R.id.con_pay);
        profile = (ConstraintLayout) rootView.findViewById(R.id.con_profile);
        notificaton = (ConstraintLayout) rootView.findViewById(R.id.con_notification);
        constraintFavourite = (ConstraintLayout) rootView.findViewById(R.id.constraintFavourite);
        share_app = (ConstraintLayout) rootView.findViewById(R.id.con_share);
        my_service = (ConstraintLayout) rootView.findViewById(R.id.con_myservice);
        terms_condition = (ConstraintLayout) rootView.findViewById(R.id.con_term_c);
        about_tameed = (ConstraintLayout) rootView.findViewById(R.id.con_about);
        contact_us = (ConstraintLayout) rootView.findViewById(R.id.con_contact);
        my_acount = (ConstraintLayout) rootView.findViewById(R.id.con_my_acct);
        logout = (ConstraintLayout) rootView.findViewById(R.id.con_logout);
        user_profile_img = (ImageView) rootView.findViewById(R.id.user_profile_img);
        notification_img = (ImageView) rootView.findViewById(R.id.img_notification);
        setting_name = (TextView) rootView.findViewById(R.id.setting_name);
        seting_email = (TextView) rootView.findViewById(R.id.setting_email);
        imageView37 = rootView.findViewById(R.id.imageView37);
        constraintLayout5 = (ConstraintLayout) rootView.findViewById(R.id.constraintLayout5);

        //ADD JAY CALL FOR UPDATE CHAT COUNT
        if (TextUtils.isEmpty(ssp.getUserId(getActivity()))) {
            helper.login = false;
        } else {
            helper.login = true;
            CallApiShowbadgecount();
        }


        if (ssp.getUser_type(getActivity()).equals("Provider")) {

            imageView37.setImageResource(R.mipmap.switchon);
        } else {
            imageView37.setImageResource(R.mipmap.switchoff);
        }


        if (helper.login == false) {
            logout.setVisibility(View.GONE);

        } else {
            logout.setVisibility(View.VISIBLE);
        }


        setting_name.setText(ssp.getName(getActivity()));
        if (ssp.getEmail(getActivity()).equals("")) {
            seting_email.setText("البريد الإلكتروني");
        } else {
            seting_email.setText(ssp.getEmail(getActivity()));
        }


        notify_save = ssp.getPush_notification(getActivity());
        ////Log.e("notify_save", notify_save);
        if (notify_save.equals("ON")) {
            notification_img.setImageResource(R.mipmap.switchon);
            bool = false;
        } else if (notify_save.equals("OFF")) {
            notification_img.setImageResource(R.mipmap.switchoff);
            bool = true;
        }
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ssp.getUserId(getActivity()).equals("")) {
                    final AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(getActivity());
                    }

                    alert.setMessage(getResources().getString(R.string.please_first_login));


                    alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().startActivity(new Intent(getActivity(), RegisterActivity.class));


                        }
                    });


                    Dialog dialog = alert.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                } else {
                    startActivity(new Intent(getActivity(), ResentChat.class));
                }

            }
        });


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (helper.login == false) {

                    final AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(getActivity());
                    }

                    alert.setMessage(getResources().getString(R.string.please_first_login));


                    alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getActivity(), RegisterActivity.class));


                        }
                    });


                    Dialog dialog = alert.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                } else {
                    startActivity(new Intent(getActivity(), tameed.com.tameed.profile.class));
                }

            }
        });

        terms_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), Terms_Condition.class));

//                Intent intent = new Intent(getActivity(), PaymentActivity.class);
//                intent.putExtra("order_id", "0");
//                intent.putExtra("provider_id", "0");
//                intent.putExtra("total_fee", "0");
//                startActivity(intent);


            }
        });

        about_tameed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), About.class));
            }
        });

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Contact_us.class));
            }
        });

        my_acount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), My_account.class));
            }
        });

        payment_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (helper.login == false) {

                    final AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(getActivity());
                    }

                    alert.setMessage(getResources().getString(R.string.please_first_login));


                    alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getActivity(), RegisterActivity.class));


                        }
                    });


                    Dialog dialog = alert.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                } else {
                    startActivity(new Intent(getActivity(), Payment_type.class));
                }
            }
        });

        constraintFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (helper.login == false) {

                    final AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(getActivity());
                    }

                    alert.setMessage(getResources().getString(R.string.please_first_login));


                    alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getActivity(), RegisterActivity.class));


                        }
                    });


                    Dialog dialog = alert.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                } else {


                    startActivity(new Intent(getActivity(), Favourite.class));


                }


            }
        });


        notificaton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                if (helper.login == false) {
//
//                    final AlertDialog.Builder alert;
//                    if (Build.VERSION.SDK_INT >= 11) {
//                        alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
//                    } else {
//                        alert = new AlertDialog.Builder(getActivity());
//                    }
//
//                    alert.setMessage(getResources().getString(R.string.please_first_login));
//
//
//                    alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            startActivity(new Intent(getActivity(), RegisterActivity.class));
//
//
//                        }
//                    });
//
//
//                    Dialog dialog = alert.create();
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.show();
//                } else {


//                    startActivity(new Intent(getActivity(), Notification.class));


                ((MainActivity) getActivity()).moveToNotification();

//                }

            }
        });
        my_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ssp.getUserId(getActivity()).equals("")) {

                    final AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(getActivity());
                    }

                    alert.setMessage(getResources().getString(R.string.please_first_login));
                    alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().startActivity(new Intent(getActivity(), RegisterActivity.class));

                        }
                    });
                    Dialog dialog = alert.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                } else {
                    Intent i = new Intent(getActivity(), My_services.class);
                    i.putExtra("p_room", "2");
                    startActivity(i);
                }
            }
        });


        // for sharing app

        share_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationInfo app = getActivity().getApplicationInfo();
                String filePath = app.sourceDir;

                Intent intent = new Intent(Intent.ACTION_SEND);

                // MIME of .apk is "application/vnd.android.package-archive".
                // but Bluetooth does not accept this. Let's use "*/*" instead.
                intent.setType("*/*");


                // Append file and send Intent
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
                startActivity(Intent.createChooser(intent, "Share app via"));
            }
        });


        // LOGOUT


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alert;
                if (Build.VERSION.SDK_INT >= 11) {
                    alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                } else {
                    alert = new AlertDialog.Builder(getActivity());
                }

                alert.setMessage("هل ترغب بتسجيل الخروج؟");


                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //prefs= getSharedPreferences(prefname, MODE_PRIVATE);

                        logout();
                    }
                });

                alert.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });

                Dialog dialog = alert.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.show();

            }
        });


        notification_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bool) {
                    notification_img.setImageResource(R.mipmap.switchon);
                    notify = "ON";
                } else {
                    notification_img.setImageResource(R.mipmap.switchoff);
                    notify = "OFF";

                }
                bool = !bool;
                notification_status();
            }
        });


        if (ssp.getUserId(getActivity()).equals("")) {
            user_profile_img.setImageResource(R.mipmap.nouser_2x);
            setting_name.setText("لايوجد اسم");
            //seting_email.setText("");
        }


        setting_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do something here
                Log.e("Name: ", ":" + helper.login);
                if (helper.login == false) {

                    final AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(getActivity());
                    }

                    alert.setMessage(getResources().getString(R.string.please_first_login));


                    alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getActivity(), RegisterActivity.class));


                        }
                    });


                    Dialog dialog = alert.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                } else {
                    startActivity(new Intent(getActivity(), tameed.com.tameed.profile.class));
                }

            }
        });

        seting_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do something here
                Log.e("Email: ", ":" + helper.login);
                if (helper.login == false) {

                    final AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(getActivity());
                    }

                    alert.setMessage(getResources().getString(R.string.please_first_login));


                    alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getActivity(), RegisterActivity.class));


                        }
                    });


                    Dialog dialog = alert.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                } else {
                    startActivity(new Intent(getActivity(), tameed.com.tameed.profile.class));
                }

            }
        });

        constraintLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do something here

                Log.e("constraintLayout: ", ":" + helper.login);
                if (helper.login == false) {

                    final AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(getActivity());
                    }

                    alert.setMessage(getResources().getString(R.string.please_first_login));


                    alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getActivity(), RegisterActivity.class));


                        }
                    });


                    Dialog dialog = alert.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                } else {
                    startActivity(new Intent(getActivity(), tameed.com.tameed.profile.class));
                }


            }
        });

        image_url = ssp.getKEY_profile_pic_thumb_url(getActivity());
        ////Log.e("hhhhhhhh", image_url);

        if (TextUtils.isEmpty(ssp.getUserId(getActivity()))) {
            helper.login = false;
        } else {
            helper.login = true;


     /*       if (!image_url.equals("")) {
                Glide.with(Setting.this)
                        .load(image_url)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                progress.setVisibility(View.GONE);
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progress.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(user_profile_img);
            }*/
            if (!image_url.equals("")) {
                Picasso.with(getActivity())
                        .load(image_url)
                        .noFade()
                        .error(R.mipmap.nouser_2x)
                        .into(user_profile_img);
            } else if (image_url.equals("")) {
                user_profile_img.setImageResource(R.mipmap.nouser_2x);
            }
        }


// user profile upload

        user_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (helper.login == false) {

                    final AlertDialog.Builder alert;
                    if (Build.VERSION.SDK_INT >= 11) {
                        alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                    } else {
                        alert = new AlertDialog.Builder(getActivity());
                    }

                    alert.setMessage(getResources().getString(R.string.please_first_login));


                    alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getActivity(), RegisterActivity.class));


                        }
                    });


                    Dialog dialog = alert.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                } else {

                    final Dialog dialog1 = new Dialog(getActivity(), R.style.DialogSlideAnim);
                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog1.setContentView(R.layout.frnd_custom);

                    dialog1.getWindow().setBackgroundDrawable(
                            new ColorDrawable(Color.TRANSPARENT));
                    dialog1.show();
                    RelativeLayout takepicture = (RelativeLayout) dialog1.findViewById(R.id.gallery_rl);
                    RelativeLayout uploadfile = (RelativeLayout) dialog1.findViewById(R.id.camera_rl);
                    RelativeLayout cancel_rl = (RelativeLayout) dialog1.findViewById(R.id.cancel_rl);
                    RelativeLayout remove_rl = (RelativeLayout) dialog1.findViewById(R.id.remove_rl);

                    remove_rl.setVisibility(View.VISIBLE);

                    cancel_rl.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();

                        }


                    });

                    remove_rl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            image_url = ssp.getKEY_profile_pic_thumb_url(getActivity());
                            if (image_url.equals("")) {
                                final AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(getActivity());
                                }

                                alert.setMessage(getResources().getString(R.string.error_no_profile_pic_to_remove));


                                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //prefs= getSharedPreferences(prefname, MODE_PRIVATE);

                                        dialog1.dismiss();
                                    }
                                });


                                Dialog dialog = alert.create();
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.show();


                            } else {


                                final AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(getActivity());
                                }

                                alert.setMessage(getResources().getString(R.string.msg_remove_picture));


                                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //prefs= getSharedPreferences(prefname, MODE_PRIVATE);
                                        remove_pic();
                                        dialog1.dismiss();


                                    }
                                });

                                alert.setNegativeButton("لا", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //prefs= getSharedPreferences(prefname, MODE_PRIVATE);

//                                finish();
                                        dialog.dismiss();
                                    }
                                });
                                Dialog dialog = alert.create();
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.show();


                            }
                        }
                    });

                    takepicture.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {


                            ACCESS_REQUST = 1;
                            checkAndroidVersion();
                            dialog1.dismiss();

                        }
                    });

                    uploadfile.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {


                            ACCESS_REQUST = 2;
                            checkAndroidVersion();
                            dialog1.dismiss();

                        }
                    });


                }
            }
        });


        return rootView;
    }


    private int PERMISSIONS_MULTIPLE_REQUEST = 5;
    int ACCESS_REQUST;

    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) + ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_MULTIPLE_REQUEST);
                }

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_MULTIPLE_REQUEST);
                }
            }
        } else if (ACCESS_REQUST == 1) {
            cameraIntent();
        } else if (ACCESS_REQUST == 2) {
            galleryIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 5:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (cameraPermission && readExternalFile && writeExternalFile) {

                        if (ACCESS_REQUST == 1) {
                            cameraIntent();
                        } else if (ACCESS_REQUST == 2) {
                            galleryIntent();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Permission Not Granted", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else if (ACCESS_REQUST == 1) {
            cameraIntent();
        } else if (ACCESS_REQUST == 2) {
            galleryIntent();
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE_RESULTS);


    }

    private void cameraIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = null;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 7);
    }


    public void onResume() {
        super.onResume();

        notify_save = ssp.getPush_notification(getActivity());
        if (notify_save.equals("ON")) {
            notification_img.setImageResource(R.mipmap.switchon);
            bool = false;
        } else if (notify_save.equals("OFF")) {
            notification_img.setImageResource(R.mipmap.switchoff);
            bool = true;
        }


        //ADD JAY CALL FOR UPDATE CHAT COUNT
        ////Log.e(TAG, "onResume>>>>Replete Call CallApiShowbadgecount");
        if (TextUtils.isEmpty(ssp.getUserId(getActivity()))) {
            helper.login = false;
        } else {
            helper.login = true;
            CallApiShowbadgecount();
        }

    }


    private void CallApiShowbadgecount() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response.toString());
                            int maxLogSize = 1000;
                            for (int i = 0; i <= response.toString().length() / maxLogSize; i++) {
                                int start1 = i * maxLogSize;
                                int end = (i + 1) * maxLogSize;
                                end = end > response.length() ? response.toString().length() : end;
                                ////Log.e("Json Data", response.toString().substring(start1, end));
                            }
                            String unread_chat_count = obj.getString("unread_chat_count");
                            String online_offline_status = obj.getString("online_offline_status");
                            String pending_orders_count_as_customer = obj.getString("pending_orders_count_as_customer");
                            String total_order_badge_count = obj.getString("total_order_badge_count");

                            ////Log.e(TAG, "unread_chat_count >>>>>>>>>>" + unread_chat_count);
                            ////Log.e(TAG, "total_order_badge_count >>>>" + total_order_badge_count);

                            //ORDER VIEW COUNT
                            if (!TextUtils.isEmpty(total_order_badge_count) && !total_order_badge_count.equals("0")) {
                                helper.badge_txt.setVisibility(View.VISIBLE);
                                helper.badge_txt.setText(total_order_badge_count);
                            }

                            //ADD JAY SETTING VIEW COUNT
                            if (!TextUtils.isEmpty(unread_chat_count) && !unread_chat_count.equals("0")) {
                                badge_txt_setting_chat.setVisibility(View.VISIBLE);
                                badge_txt_setting_chat.setText(unread_chat_count);

                                //ALSO RESET SETTING ICON COUNT
                                helper.badge_txt_setting.setText(unread_chat_count);
                            } else {
                                badge_txt_setting_chat.setVisibility(View.GONE);
                                helper.badge_txt_setting.setVisibility(View.GONE);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast.makeText(getActivity(), getResources().getString(R.string.login_error), Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof ServerError) {

                            Toast.makeText(getActivity(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.networkError_Message), Toast.LENGTH_LONG).show();

                            //TODO

                        } else if (error instanceof ParseError) {


                        }

                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "Showbadgecount");
                params.put("user_id", ssp.getUserId(mContext));
                ////Log.e("params", params.toString());
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");

            // user_profile_img.setImageBitmap(bitmap);
            loadData();

        }
        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && data != null) {

            Uri selectedImageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);

                loadData();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (null != selectedImageUri) {
                String path = getPathFromURI(selectedImageUri);
                if (path != null)
                    bitmap = getBitmapFromURL(path);
            }
        }

    }

    private void loadData() {
        if (asyncLoad == null
                || asyncLoad.getStatus() != AsyncTask.Status.RUNNING) {
            asyncLoad = new AsyncLoadData();
            asyncLoad.execute();
        }
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);

        if (cursor == null) {
            ////Log.e("11wwww......", "......");
            res = contentUri.getPath();
        } else {
            ////Log.e("22wwww......", "......");

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            res = cursor.getString(column_index);
            cursor.close();

        }


        return res;
    }

    @SuppressLint("InlinedApi")
    private void showProgDialog() {
        progress_dialog = null;
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                progress_dialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
            } else {
                progress_dialog = new ProgressDialog(getActivity());
            }
            progress_dialog.setMessage(String.valueOf(R.string.uploading));
            progress_dialog.setCancelable(false);
            progress_dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // hide progress
    private void hideProgDialog() {
        try {
            if (progress_dialog != null && progress_dialog.isShowing())
                progress_dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            ////Log.e("Data", "<><>" + response);
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            ////Log.e("Data", "<><>" + obj.toString());
                            String message = obj.getString("msg");
                            ////Log.e("msg", "==" + message);

                            if (message.equals("Logout successfully")) {
                                final AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(getActivity());
                                }
                                alert.setMessage("تم تسجيل الخروج بنجاح");
                                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        ssp.setUserId(getActivity(), "");

                                        //ADD JAY
                                        ssp.setName(getActivity(), "");
                                        ssp.setEmail(getActivity(), "");
                                        ssp.setMobile_number(getActivity(), "");
                                        ssp.setCombine_Mobile_number(getActivity(), "");
                                        ssp.setLatitude(getActivity(), "");
                                        ssp.setLongitude(getActivity(), "");
                                        ssp.setLocation(getActivity(), "");
                                        ssp.setLogin_status(getActivity(), "");
                                        ssp.setPayment_preference(getActivity(), "");
                                        ssp.setActive_status(getActivity(), "");
                                        ssp.setUser_type(getActivity(), "");
                                        ssp.setKEY_profile_pic_thumb_url(getActivity(), "");
                                        ssp.setPush_notification(getActivity(), "");
                                        ssp.setOnline_offline_status(getActivity(), "");
                                        ssp.setDescription(getActivity(), "");
                                        ssp.setAdded_date(getActivity(), "");
                                        ssp.setReview_count(getActivity(), "");
                                        ssp.setReview_rating(getActivity(), "");
                                        ssp.setOrder_count(getActivity(), "");
                                        ssp.setCountry(getActivity(), "");
                                        ssp.setMobile_visible(getActivity(), "");
                                        ssp.setCity_to_cover(getActivity(), "");
                                        ssp.setDistance(getActivity(), "");
                                        ssp.setUser_bank_name(getActivity(), "");
                                        ssp.setUser_bank_acct_num(getActivity(), "");
                                        ssp.setUser_bank_iban_num(getActivity(), "");
                                        ssp.setKEY_profile_pic_thumb_url(getActivity(), "");


                                        ////Log.e("CLEAR USER_ID", "==>>>>" + ssp.getUserId(getActivity()));
                                        startActivity(new Intent(getActivity(), RegisterActivity.class));
                                    }
                                });
                                Dialog dialog = alert.create();
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "Logout");
                params.put("user_id", ssp.getUserId(getActivity()));
                ////Log.e("params", "===" + params.toString());
                return params;
            }

        };

//        Singleton.getInstance(this).addToRequestQueue(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);

    }

    public void remove_pic() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            ////Log.e("Data", "<><>" + response);
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            ////Log.e("Data", "<><>" + obj.toString());
                            String message = obj.getString("msg");
                            ////Log.e("msg", "==" + message);

                            if (message.equals("Image removed successfully")) {

                                JSONObject jarray = obj.getJSONObject("user_details");

                                if (obj.has("user_details")) {
                                    pic_thumb = jarray.getString("profile_pic_thumb_url");

                                    ssp.setKEY_profile_pic_thumb_url(getActivity(), pic_thumb);
                                }
                                final AlertDialog.Builder alert;
                                if (Build.VERSION.SDK_INT >= 11) {
                                    alert = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
                                } else {
                                    alert = new AlertDialog.Builder(getActivity());
                                }

                                alert.setMessage(getResources().getString(R.string.msg_image_removed_successfully));


                                alert.setPositiveButton("نعم", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //prefs= getSharedPreferences(prefname, MODE_PRIVATE);

                                        Intent i = new Intent(getActivity(), MainActivity.class);
                                        i.putExtra("flag", "4");
                                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivityForResult(i, 0);

                                    }
                                });


                                Dialog dialog = alert.create();
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "Removeimage");
                params.put("user_id", ssp.getUserId(getActivity()));
                ////Log.e("params", "===" + params.toString());
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void notification_status() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Apis.api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            ////Log.e("Data", "<><>" + response);
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            ////Log.e("Data", "<><>" + obj.toString());
                            String message = obj.getString("msg");
                            String status = obj.getString("status");
                            ////Log.e("status", "==" + status);


                            if (message.equals("Status updated successfully")) {
                                ssp.setPush_notification(getActivity(), status);

                                JSONObject jarray = obj.getJSONObject("user_details");

                                if (obj.has("user_details")) {
                                    notify_update = jarray.getString("push_notification");

                                    ////Log.e("notifyup", notify_update);
//                                    ssp.setPush_notification(getActivity(),notify_update);

                                }

                                if (notify.equals("ON")) {


                                    Toast.makeText(getActivity(), "Notification: ON", Toast.LENGTH_SHORT).show();

                                } else if (notify.equals("OFF")) {


                                    Toast.makeText(getActivity(), "Notification : OFF", Toast.LENGTH_SHORT).show();

                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("action", "Notificationsetting");

                params.put("user_id", ssp.getUserId(getActivity()));
                params.put("status", notify);


                ////Log.e("params", "===" + params.toString());
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(stringRequest);


    }

    class AsyncLoadData extends AsyncTask<String, Void, Void> {
        boolean flag = false;

        @Override
        protected Void doInBackground(String... strings) {
            mHandler.sendEmptyMessage(SHOW_PROG_DIALOG);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);


            picture = baos.toByteArray();
            LoadApi api = new LoadApi();

            try {
                if (picture != null) {

                    ////Log.e("action", "" + action);
                    ////Log.e("user_id", "" + user_id);
                    ////Log.e("55555", "" + picture);
                    jobj = api.Action_profileSetting_Image(action, user_id, picture);

                }

                JSONObject object = api.getResult1();
                ////Log.e("Profile_Setting", ">>" + object);
                msg = object.getString("msg");
                ////Log.e("Meassage", "  " + msg);

                if (object.has("pic_url") && object.has("pic_thumb_url")) {
                    pic_url = object.getString("pic_url");
                    ////Log.e("pic_url", ">>" + pic_url);
                    pic_thumb_url = object.getString("pic_thumb_url");
                } else {
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mHandler.sendEmptyMessage(HIDE_PROG_DIALOG);
            if (msg.equals("Your picture has been uploaded successfully")) {

            /*    if (!pic_url.equals("")) {
                    Glide.with(Setting.this)
                            .load(pic_url)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    progress.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    progress.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(user_profile_img);
                }*/

                Picasso.with(getActivity())
                        .load(pic_url)
                        .noFade()
                        .error(R.mipmap.nouser_2x)
                        .into(user_profile_img);
                ssp.setKEY_profile_pic_thumb_url(getActivity(), pic_url);


            }

            super.onPostExecute(aVoid);
        }
    }

    public void EnableRuntimePermission() {

        ////Log.e("Enable...111......", ".......");
        if (
                ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            ////Log.e("Enable......22...", ".......");
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {
                            //android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE
                            android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, RequestPermissionCode);
            return;
        }

    }

}
