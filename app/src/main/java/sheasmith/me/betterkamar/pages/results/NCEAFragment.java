package sheasmith.me.betterkamar.pages.results;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;
import java.util.HashSet;

import sheasmith.me.betterkamar.ApiManager;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.GroupObject;
import sheasmith.me.betterkamar.dataModels.NCEAObject;
import sheasmith.me.betterkamar.dataModels.NoticesObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.PortalObject;
import sheasmith.me.betterkamar.pages.notices.NoticesAdapter;

import static android.content.Context.MODE_PRIVATE;

public class NCEAFragment extends Fragment {

    private View mView;
    private PortalObject mPortal;

    public static NCEAFragment newInstance() {
        return new NCEAFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final PortalObject portal = (PortalObject) getActivity().getIntent().getSerializableExtra("portal");
        mPortal = portal;
        ApiManager.setVariables(portal, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_results_ncea, container, false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                doRequest(mPortal);
            }
        });

        return mView;
    }

    private void doRequest(PortalObject portal) {
        ApiManager.getNCEADetails(new ApiResponse<NCEAObject>() {
            @Override
            public void success(NCEAObject value) {
                NCEAObject.Student student = value.StudentNCEASummaryResults.Student;
                ((TextView) mView.findViewById(R.id.total_achieved)).setText(student.CreditsTotal.Achieved);
                ((TextView) mView.findViewById(R.id.total_merit)).setText(student.CreditsTotal.Merit);
                ((TextView) mView.findViewById(R.id.total_excellence)).setText(student.CreditsTotal.Excellence);
                ((TextView) mView.findViewById(R.id.total_credits)).setText(student.CreditsTotal.Total);
                ((TextView) mView.findViewById(R.id.total_attempted)).setText(student.CreditsTotal.Attempted);

                ((TextView) mView.findViewById(R.id.internal_achieved)).setText(student.CreditsInternal.Achieved);
                ((TextView) mView.findViewById(R.id.internal_merit)).setText(student.CreditsInternal.Merit);
                ((TextView) mView.findViewById(R.id.internal_excellence)).setText(student.CreditsInternal.Excellence);
                ((TextView) mView.findViewById(R.id.internal_credits)).setText(student.CreditsInternal.Total);
                ((TextView) mView.findViewById(R.id.internal_attempted)).setText(student.CreditsInternal.Attempted);

                ((TextView) mView.findViewById(R.id.external_achieved)).setText(student.CreditsExternal.Achieved);
                ((TextView) mView.findViewById(R.id.external_merit)).setText(student.CreditsExternal.Merit);
                ((TextView) mView.findViewById(R.id.external_excellence)).setText(student.CreditsExternal.Excellence);
                ((TextView) mView.findViewById(R.id.external_credits)).setText(student.CreditsExternal.Total);
                ((TextView) mView.findViewById(R.id.external_attempted)).setText(student.CreditsExternal.Attempted);

                NCEAObject.YearTotal latest = student.YearTotals.get(0);
                int excellence = Integer.parseInt(latest.Excellence);
                int merit = Integer.parseInt(latest.Merit);
                int achieved = Integer.parseInt(latest.Achieved);
                int lastAchieved = Integer.parseInt(student.YearTotals.get(1).Achieved);

                // Account for up to 20 credits gained in the previous year
                if (achieved + lastAchieved < 80)
                    achieved = achieved + lastAchieved;

                if (lastAchieved > 20)
                    lastAchieved = 20;

                if (excellence < 50)
                    excellence = 50 - excellence;
                else
                    excellence = 0;

                if (merit < 50)
                    merit = 50 - merit;
                else
                    merit = 0;

                if (achieved < 80)
                    achieved = 80 - achieved;
                else
                    achieved = 0;

                ((TextView) mView.findViewById(R.id.needed_excellence)).setText(Integer.toString(excellence));
                ((TextView) mView.findViewById(R.id.needed_merit)).setText(Integer.toString(merit));
                ((TextView) mView.findViewById(R.id.needed_pass)).setText(Integer.toString(achieved));

                ((TextView) mView.findViewById(R.id.current_year_achieved)).setText(latest.Achieved);
                ((TextView) mView.findViewById(R.id.current_year_achieved)).setText(latest.Merit);
                ((TextView) mView.findViewById(R.id.current_year_achieved)).setText(latest.NotAchieved);
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
