package playment.scrollanddrag.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.List;

import playment.scrollanddrag.R;
import playment.scrollanddrag.adapter.ImageAdapter;
import playment.scrollanddrag.commons.ImageDragCallback;

public class HomeActivity extends AppCompatActivity implements ImageDragCallback {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Integer[] drawables;
    private List<Integer> drawablesList;
    private ImageView resImageView;
    private ImageAdapter imageAdapter;
    private ImageDragCallback imageDragCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = (RecyclerView) findViewById(R.id.images_list);
        resImageView = (ImageView) findViewById(R.id.drag_image);
        layoutManager = new LinearLayoutManager(HomeActivity.this,LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        imageDragCallback = this;

        drawables = new Integer[]{
                R.mipmap.bagonia,
                R.mipmap.bouquet,
                R.mipmap.bouquet2,
                R.mipmap.vase_1
        };
        drawablesList = Arrays.asList(drawables);
        imageAdapter = new ImageAdapter(this,drawablesList,imageDragCallback);
        recyclerView.setAdapter(imageAdapter);
    }

    @Override
    public void onImageSelected(int drawableRes) {
        resImageView.setImageResource(drawableRes);
    }
}
