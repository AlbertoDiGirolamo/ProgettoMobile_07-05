package com.example.progettomobile_07_05;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.progettomobile_07_05.Database.CardItem;
import com.example.progettomobile_07_05.Database.CardItemRepository;
import com.example.progettomobile_07_05.Database.User;
import com.example.progettomobile_07_05.ViewModel.ListViewModel;

import java.util.List;

public class DetailsFragment extends Fragment {

    private TextView nameTextView;
    private TextView descriptionTextView;
    private TextView priceTextView;
    private TextView positionTextView;

    private ImageView productImageView;
    private ListViewModel listViewModel;


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
                    priceTextView.setText(cardItem.getProductPrice()+" €");
                    positionTextView.setText(cardItem.getProductPosition());
                    String image_path = cardItem.getImageResource();


                    if (image_path.contains("ic_")){
                        Drawable drawable = ResourcesCompat.getDrawable(activity.getResources(),
                                R.drawable.verdura, activity.getTheme());
                        productImageView.setImageDrawable(drawable);
                    } else {
                        Bitmap bitmap = Utilities.getImageBitmap(activity, Uri.parse(image_path));
                        if (bitmap != null){
                            productImageView.setImageBitmap(bitmap);
                            productImageView.setBackgroundColor(Color.WHITE);
                        }
                        else{
                            Drawable drawable = ResourcesCompat.getDrawable(activity.getResources(),
                                    R.drawable.verdura, activity.getTheme());
                            productImageView.setImageDrawable(drawable);
                        }
                    }

                    FragmentManager fm = getFragmentManager();
                    int count = getFragmentManager().getBackStackEntryCount();
                    try {
                        String name = fm.getBackStackEntryAt(count - 2).getName();
                        //Log.d("elimina",name );
                        if (!name.equals("MyProductFragment")) {
                            getActivity().findViewById(R.id.deleteitem).setVisibility(View.INVISIBLE);
                        }
                    }catch (Exception e){
                        getActivity().findViewById(R.id.deleteitem).setVisibility(View.INVISIBLE);
                    }
                    getActivity().findViewById(R.id.deleteitem).setOnClickListener(l->{
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        CardItemRepository cardItemRepository = new CardItemRepository(activity.getApplication());
                                        cardItemRepository.deleteItem(cardItem.getId());

                                        Utilities.insertFragment((AppCompatActivity) getActivity(), new MyProductFragment(), MyProductFragment.class.getSimpleName());
                                        Toast.makeText(getActivity(), "Prodotto eliminato con successo", Toast.LENGTH_SHORT).show();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:

                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Sei sicuro di voler eliminare il prodotto?").setPositiveButton("Si", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();

                    });


                }

            });

            view.findViewById(R.id.share_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Ciao! Dai un'occhiata a questo prodotto: " +nameTextView.getText().toString() +
                          ".\n" + "Costa: " +
                            priceTextView.getText().toString()+ "/Kg" +"\n" + "Descrizione: " +
                            descriptionTextView.getText().toString() + ".\nSi trova: "+
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
