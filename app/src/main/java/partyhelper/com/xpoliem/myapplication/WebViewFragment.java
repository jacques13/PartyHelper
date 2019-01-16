package partyhelper.com.xpoliem.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class WebViewFragment extends Fragment {
    WebView webView;

	public final static String TAG = WebViewFragment.class.getSimpleName();
	
	public WebViewFragment() {
		// TODO Auto-generated constructor stub
	}

	public static WebViewFragment newInstance() {
		return new WebViewFragment();
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_webview, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		 webView = (WebView) view.findViewById(R.id.webView);
        ((MainActivity) getActivity()).internetFragTest();
		webView.loadUrl("http://xpoliem.com/adds/KonnectAd.html");

	}




}
