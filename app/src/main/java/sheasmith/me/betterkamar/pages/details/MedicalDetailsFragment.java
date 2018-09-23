package sheasmith.me.betterkamar.pages.details;

import android.content.DialogInterface;
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

import sheasmith.me.betterkamar.ApiManager;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.DetailsObject;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.PortalObject;

import static android.view.View.GONE;

public class MedicalDetailsFragment extends Fragment {

    private View mView;
    private ProgressBar mLoader;
    private PortalObject mPortal;

    public static MedicalDetailsFragment newInstance() {
        return new MedicalDetailsFragment();
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

        mView = inflater.inflate(R.layout.fragment_details_medical, container, false);
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

                       setText(student.AllowedPanadol, R.id.general_information_allowed_panadol, R.id.general_information_allowed_panadol_heading);
                       setText(student.AllowedIbuprofen, R.id.general_information_allowed_ibuprofen, R.id.general_information_allowed_ibuprofen_heading);
                       setText(student.Medical, R.id.general_information_medical_conditions, R.id.general_information_medical_conditions_heading);
                       setText(student.Reactions, R.id.general_information_reactions, R.id.general_information_reactions_heading);
                       setText(student.Vaccinations, R.id.general_information_vaccinations, R.id.general_information_vaccinations_heading);
                       setText(student.SpecialCircumstances, R.id.general_information_special_circumstances, R.id.general_information_special_circumstances_heading);
                       setText(student.GeneralNotes, R.id.general_information_general_notes, R.id.general_information_general_notes_heading);
                       setText(student.HealthNotes, R.id.general_information_health_notes, R.id.general_information_health_notes_heading);
                       maybeHideView(R.id.general_information, student.AllowedPanadol, student.AllowedIbuprofen, student.Medical, student.MotherEmail, student.Reactions, student.Vaccinations, student.SpecialCircumstances, student.GeneralNotes, student.HealthNotes);

                       setText(student.DoctorName, R.id.doctor_name, R.id.doctor_name_heading);
                       setText(student.DoctorPhone, R.id.doctor_phone, R.id.doctor_phone_heading);
                       setText(student.DoctorAddress, R.id.doctor_address, R.id.doctor_address_heading);
                       maybeHideView(R.id.doctor, student.DoctorName, student.DoctorPhone, student.DoctorAddress);

                       setText(student.DentistName, R.id.dentist_name, R.id.dentist_name_heading);
                       setText(student.DentistPhone, R.id.dentist_phone, R.id.dentist_phone_heading);
                       setText(student.DentistAddress, R.id.dentist_address, R.id.dentist_address_heading);
                       maybeHideView(R.id.dentist, student.DentistName, student.DentistPhone, student.DentistAddress);

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
