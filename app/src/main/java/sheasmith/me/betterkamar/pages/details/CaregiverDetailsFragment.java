package sheasmith.me.betterkamar.pages.details;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import sheasmith.me.betterkamar.ApiManager;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.DetailsObject;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.PortalObject;

import static android.view.View.GONE;

public class CaregiverDetailsFragment extends Fragment {

    private View mView;
    private ProgressBar mLoader;
    private PortalObject mPortal;

    public static CaregiverDetailsFragment newInstance() {
        return new CaregiverDetailsFragment();
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

        mView = inflater.inflate(R.layout.fragment_details_caregivers, container, false);
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

                       setText(student.MotherName, R.id.parent_a_heading);
                       setText(student.MotherRelation, R.id.parent_a_relationship);
                       setText(student.MotherStatus, R.id.parent_a_status, R.id.parent_a_status_heading);
                       setText(student.MotherEmail, R.id.parent_a_email, R.id.parent_a_email_heading);
                       setText(student.MotherPhoneHome, R.id.parent_a_home_phone, R.id.parent_a_home_phone_heading);
                       setText(student.MotherPhoneCell, R.id.parent_a_mobile_phone, R.id.parent_a_mobile_phone_heading);
                       setText(student.MotherPhoneWork, R.id.parent_a_work_phone, R.id.parent_a_work_phone_heading);
                       setText(student.MotherPhoneExtn, R.id.parent_a_extension, R.id.parent_a_extension_heading);
                       setText(student.MotherOccupation, R.id.parent_a_occupation, R.id.parent_a_occupation_heading);
                       setText(student.MotherNotes, R.id.parent_a_notes, R.id.parent_a_notes_heading);
                       maybeHideView(R.id.parent_a, student.MotherName, student.MotherRelation, student.MotherStatus, student.MotherEmail, student.MotherPhoneHome, student.MotherPhoneCell, student.MotherPhoneWork, student.MotherPhoneExtn, student.MotherOccupation, student.MotherNotes);

                       setText(student.FatherName, R.id.parent_b_heading);
                       setText(student.FatherRelation, R.id.parent_b_relationship);
                       setText(student.FatherStatus, R.id.parent_b_status, R.id.parent_b_status_heading);
                       setText(student.FatherEmail, R.id.parent_b_email, R.id.parent_b_email_heading);
                       setText(student.FatherPhoneHome, R.id.parent_b_home_phone, R.id.parent_b_home_phone_heading);
                       setText(student.FatherPhoneCell, R.id.parent_b_mobile_phone, R.id.parent_b_mobile_phone_heading);
                       setText(student.FatherPhoneWork, R.id.parent_b_work_phone, R.id.parent_b_work_phone_heading);
                       setText(student.FatherPhoneExtn, R.id.parent_b_extension, R.id.parent_b_extension_heading);
                       setText(student.FatherOccupation, R.id.parent_b_occupation, R.id.parent_b_occupation_heading);
                       setText(student.FatherNotes, R.id.parent_b_notes, R.id.parent_b_notes_heading);
                       maybeHideView(R.id.parent_b, student.FatherName, student.FatherRelation, student.FatherStatus, student.FatherEmail, student.FatherPhoneHome, student.FatherPhoneCell, student.FatherPhoneWork, student.FatherPhoneExtn, student.FatherOccupation, student.FatherNotes);

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
    }
}
