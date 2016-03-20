package fp.jutaporn_a.fprestaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by jutaporn-a on 20/3/2559.
 */
public class FoodAdapter  extends BaseAdapter{


    //Explicit
    private Context context;
    private String[] urlIconStrings,nameFoodStrings,priceStrings;

    public FoodAdapter(Context context,
                       String[] urlIconStrings,
                       String[] nameFoodStrings,
                       String[] priceStrings) {
        this.context = context;
        this.urlIconStrings = urlIconStrings;
        this.nameFoodStrings = nameFoodStrings;
        this.priceStrings = priceStrings;

    }

    @Override
    public int getCount() {
        return urlIconStrings.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = layoutInflater.inflate(R.layout.food_listview, viewGroup,false);

        //for image

        ImageView iconImageView = (ImageView) view1.findViewById(R.id.imageView2);
        Picasso.with(context).load(urlIconStrings[i]).resize(120,120).into(iconImageView);

        //for text
        TextView foodNameTextView = (TextView) view1.findViewById(R.id.textView2);
        foodNameTextView.setText(nameFoodStrings[i]);
        TextView priceTextView = (TextView) view1.findViewById(R.id.textView3);
        priceTextView.setText(priceStrings[i]);


        return view1;
    }
} // Main Class
