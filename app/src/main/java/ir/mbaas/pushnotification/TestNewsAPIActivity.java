package ir.mbaas.pushnotification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import ir.mbaas.sdk.apis.REST;
import ir.mbaas.sdk.listeners.IRestCallback;

public class TestNewsAPIActivity extends AppCompatActivity implements IRestCallback {

    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_api);

        tvResult = (TextView) findViewById(R.id.tv_result);

        loadLastNewsUI();
        loadNewsContent();
    }

    private void loadNewsContent() {
        Button btnNewsContent = (Button) findViewById(R.id.btn_load_news_content);
        btnNewsContent.setOnClickListener(new View.OnClickListener() {
            final EditText etNewsId = (EditText) findViewById(R.id.et_news_id);

            @Override
            public void onClick(View v) {
                String url = "http://mbaas.ir/api/mobile/content/NewsContent";
                JSONObject jsonObject = new JSONObject();
                try {
                    String newsId = etNewsId.getText().toString();
                    jsonObject.put("Id", newsId == null ? "" : newsId);

                    REST rest = new REST(url, jsonObject, TestNewsAPIActivity.this);
                    rest.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadLastNewsUI() {
        final Spinner spnRssSrc = (Spinner) findViewById(R.id.spn_rss_src);
        ArrayAdapter<CharSequence> adapterRssSrc = ArrayAdapter.createFromResource(
                this, R.array.rss_group, android.R.layout.simple_spinner_item);
        adapterRssSrc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRssSrc.setAdapter(adapterRssSrc);

        final Spinner spnContentSrc = (Spinner) findViewById(R.id.spn_content_src);
        ArrayAdapter<CharSequence> adapterContentSrc = ArrayAdapter.createFromResource(
                this, R.array.content_source, android.R.layout.simple_spinner_item);
        adapterContentSrc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnContentSrc.setAdapter(adapterContentSrc);

        final EditText etPageSize = (EditText) findViewById(R.id.et_page_size);
        final EditText etPage = (EditText) findViewById(R.id.et_page);

        Button btnLoadNews = (Button) findViewById(R.id.btn_load_news);

        btnLoadNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://mbaas.ir/api/mobile/content/news";
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("ContentSource", spnContentSrc.getSelectedItemPosition());
                    jsonObject.put("ContentSubGroup", spnRssSrc.getSelectedItemPosition());

                    String pageStr = etPage.getText().toString();
                    jsonObject.put("Page", pageStr == null || pageStr.isEmpty() ? 1 :
                            Integer.parseInt(pageStr));

                    String pageSizeStr = etPageSize.getText().toString();
                    int pageSize = pageSizeStr == null || pageSizeStr.isEmpty() ? 10 :
                            Integer.parseInt(pageStr);

                    jsonObject.put("PageSize", pageSize > 25 ? 25 : pageSize);

                    REST rest = new REST(url, jsonObject, TestNewsAPIActivity.this);
                    rest.execute();

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NumberFormatException pfe) {
                    pfe.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onSuccess(String result) {
        if (tvResult != null)
            tvResult.setText(Html.fromHtml(result));
    }

    @Override
    public void onError(Exception e) {

    }
}
