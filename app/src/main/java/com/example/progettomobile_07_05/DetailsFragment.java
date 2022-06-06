package com.example.progettomobile_07_05;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.progettomobile_07_05.Database.CardItem;
import com.example.progettomobile_07_05.ViewModel.ListViewModel;

public class DetailsFragment extends Fragment {

    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView priceTextView;
    private TextView positionTextView;

    private ImageView productImageView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        if(activity != null){
            SearchView sv = activity.findViewById(R.id.search_icon);
            sv.setVisibility(View.INVISIBLE);
            //Utilities.setUpToolbar((AppCompatActivity) activity, "Details");

            nameTextView = view.findViewById(R.id.product_name);
            descriptionTextView = view.findViewById(R.id.product_description);
            priceTextView = view.findViewById(R.id.product_price);
            positionTextView = view.findViewById(R.id.product_position);
            productImageView = view.findViewById(R.id.product_image);

            ListViewModel listViewModel =
                    new ViewModelProvider((ViewModelStoreOwner) activity).get(ListViewModel.class);
            listViewModel.getItemSelected().observe(getViewLifecycleOwner(), new Observer<CardItem>() {
                @Override
                public void onChanged(CardItem cardItem) {
                    nameTextView.setText(cardItem.getProductName());
                    descriptionTextView.setText(cardItem.getProductDescription());
                    priceTextView.setText(cardItem.getProductPrice());
                    positionTextView.setText(cardItem.getProductPosition());
                    String image_path = cardItem.getImageResource();
                    if (image_path.contains("ic_")){
                        Drawable drawable = ResourcesCompat.getDrawable(activity.getResources(),
                                R.drawable.ic_baseline_android_24, activity.getTheme());
                        productImageView.setImageDrawable(drawable);
                    } else {
                        Bitmap bitmap = Utilities.getImageBitmap(activity, Uri.parse(image_path));
                        if (bitmap != null){
                            productImageView.setImageBitmap(bitmap);
                            productImageView.setBackgroundColor(Color.WHITE);
                        }
                    }
                }
            });

            view.findViewById(R.id.share_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, getText(R.string.product_name) + ": " +
                          "\n" + getText(R.string.price) + ": " +
                            priceTextView.getText().toString() +"\n" + getText(R.string.description) + ": " +
                            descriptionTextView.getText().toString() + getText(R.string.position) + ": " +
                            positionTextView.getText().toString());
                    shareIntent.setType("text/plain");
                    Context context = view.getContext();
                    if (context != null && shareIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(Intent.createChooser(shareIntent, null));
                    }
                }
            });

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Activity activity = getActivity();
        SearchView sv = activity.findViewById(R.id.search_icon);
        sv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

       // menu.findItem(R.id.search_icon).setVisible(false);
    }
}
