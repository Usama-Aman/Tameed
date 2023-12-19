package tameed.com.tameed.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tameed.com.tameed.Entity.All_Quotation_Class;
import tameed.com.tameed.R;

public class SpinnerQuotation extends BaseAdapter {

    LayoutInflater inflater;
    Activity act;
    ArrayList<All_Quotation_Class> modelcountrylists;


    public SpinnerQuotation(Activity act, ArrayList<All_Quotation_Class> modelcountrylists) {
        this.act = act;
        this.modelcountrylists = modelcountrylists;

        inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Log.e("SpinnerQuotation", "****************************");


    }

    @Override
    public int getCount() {
        return modelcountrylists.size();
    }

    @Override
    public Object getItem(int position) {
        return modelcountrylists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getCustomView(position, convertView, parent);
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {
//        abstract_global = new Concreate_global();
//        abstract_common = new Concreate_common();

        final int pos = position;
        //  final All_Quotation_Class model = All_Quotation_Class.get;

        View row = inflater.inflate(R.layout.sp_operator, parent, false);
        TextView tv_spinner = (TextView) row.findViewById(R.id.tv_spinner);
        TextView tv_sel = (TextView) row.findViewById(R.id.tv_sel);
        //   abstract_global.abstract_fonttextview(tv_spinner, Fonts.fontitype);

        tv_spinner.setTextColor(act.getResources().getColor(R.color.Black));
        if (modelcountrylists.size() <= 1) {
            tv_spinner.setText(modelcountrylists.get(position).getName() );
        } else {
            tv_spinner.setText(modelcountrylists.get(position).getName());
        }
        return row;
    }
}
