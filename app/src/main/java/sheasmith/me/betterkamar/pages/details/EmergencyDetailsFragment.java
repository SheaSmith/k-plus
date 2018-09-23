package sheasmith.me.betterkamar.pages.details;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import sheasmith.me.betterkamar.ApiManager;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.DetailsObject;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.PortalObject;

import static android.view.View.GONE;

public class EmergencyDetailsFragment extends Fragment {

    private View mView;
    private ProgressBar mLoader;
    private PortalObject mPortal;

    public static EmergencyDetailsFragment newInstance() {
        return new EmergencyDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final PortalObject portal = (PortalObject) getActivity().getIntent().getSerializableExtra("portal");
        mPortal = portal;
        ApiManager.setVariables(portal, getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                doRequest(mPortal);
            }
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_details_emergency, container, false);
        mLoader = mView.findViewById(R.id.loader);



        return mView;
    }

    private void doRequest(final PortalObject portal) {
       ApiManager.getDetails(new ApiResponse<DetailsObject>() {
           @Override
           public void success(final DetailsObject value) {
               if (getActivity() == null) {
                   // TODO: Error?
                   return;
               }
               getActivity().runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       DetailsObject.Student student = value.StudentDetailsResults.Student;

                       setText(student.EmergencyName, R.id.emergency_heading);
                       setText(student.EmergencyPhoneHome, R.id.emergency_home_phone, R.id.emergency_home_phone_heading);
                       setText(student.EmergencyPhoneCell, R.id.emergency_mobile_phone, R.id.emergency_mobile_phone_heading);
                       setText(student.EmergencyPhoneWork, R.id.emergency_work_phone, R.id.emergency_work_phone_heading);
                       setText(student.EmergencyPhoneExtn, R.id.emergency_extension, R.id.emergency_extension_heading);
                       maybeHideView(R.id.emergency, student.EmergencyName, student.EmergencyPhoneHome, student.EmergencyPhoneCell, student.EmergencyPhoneWork, student.EmergencyPhoneExtn);

                       mLoader.setVisibility(GONE);
                   }
               });
           }

           @Override
           public void error(Exception e) {
               e.printStackTrace();
               if (e instanceof Exceptions.ExpiredToken) {
                   ApiManager.login(portal.username, portal.password, new ApiResponse<LoginObject>() {
                       @Override
                       public void success(LoginObject value) {
                           doRequest(portal);
                       }

                       @Override
                       public void error(Exception e) {
                           e.printStackTrace();
                       }
                   });
                   return;
               } else if (e instanceof IOException) {
                   getActivity().runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           new AlertDialog.Builder(getContext())
                                   .setTitle("No Internet")
                                   .setMessage("You do not appear to be connected to the internet. Please check your connection and try again.")
                                   .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialogInterface, int i) {
                                           doRequest(portal);
                                       }
                                   })
                                   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialogInterface, int i) {
                                           getActivity().finish();
                                       }
                                   })
                                   .create()
                                   .show();
                       }
                   });
               }
           }
       });
    }

    private void setText(String text, int id) {
        TextView view = mView.findViewById(id);
        if (text.equals(""))
            view.setVisibility(GONE);
        else
            view.setText(text);
    }

    private void setText(String text, int id, int header) {
        TextView view = mView.findViewById(id);
        if (text.equals("")) {
            view.setVisibility(GONE);
            mView.findViewById(header).setVisibility(GONE);
        }
        else
            view.setText(text);
    }

    private void maybeHideView(int id, String... items) {
        for (String s : items) {
            if (!s.equals(""))
                return;
        }
        mView.findViewById(id).setVisibility(GONE);
        TextView none = new TextView(getContext());
        none.setText("No emergency contact has been associated. Contact your school for more information.");
        ((LinearLayout) mView).addView(none);
    }
}
