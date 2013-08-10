﻿package com.guest.cnbeta.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.guest.cnbeta.database.ArticleDB;
import com.guest.cnbeta.loadsource.LoadSource;
import com.guest.cnbeta.loadsource.LoadSourceInterface;
import com.guest.cnbeta.module.Article;

public class ArticleListService extends BaseService {
	private LoadSourceInterface loadSource = new LoadSource();
	private ArticleDB articleDB;

	public ArticleListService(Context context) {
		super(context);
		articleDB = new ArticleDB(context);
	}

	public List<Article> getArticleListFromWeb() throws MalformedURLException,
			IOException {
		return loadSource.getArticleListFromWeb();
	}

	public List<Article> getArticleList(int page) {

		List<Article> list = null;

		try {
			list = loadSource.getMoreArticleListFromWeb(page);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return list;
	}

	public void syncArticleToDb(List<Article> list) {
		for (Article article : list) {
			articleDB.save(article);
		}
	}
	
	public List<Article> getArticleListFromDB() {
		return articleDB.getList();
	}

	public void offLineDownload(Context context, List<Article> articleList) {

		ArticleService cbArticleService = new ArticleService(context);

		try {
			for (Article article : articleList) {
				if (!article.isContented()) {
					Article mArticle = cbArticleService.getMoreDetail(article);
					FileOutputStream out = context.openFileOutput("cache_art_"
							+ mArticle.getId() + ".html", Context.MODE_PRIVATE);
					if (mArticle.getContent() != null) {
						out.write(mArticle.getContent().getBytes());
					} else {
						out.write("<html>404</html>".getBytes());
					}
					out.flush();
					out.close();

					WebView webView = new WebView(context);
					LayoutParams layoutParams = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					webView.setLayoutParams(layoutParams);
					webView.getSettings()
							.setCacheMode(WebSettings.LOAD_NORMAL);
					webView.getSettings().setLoadsImagesAutomatically(true);
					webView.loadUrl("file://" + context.getFilesDir()
							+ "/cache_art_" + article.getId() + ".html");
				}
			}

			syncArticleToDb(articleList);
			
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
