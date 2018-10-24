package com.liferay.documentation.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class NumberImagesTaskDevSite extends Task {
	
	@Override
	public void execute() throws BuildException {
		
String productType = _productType;

		List<String> dirTypes = new ArrayList<String>();
		dirTypes.add("");

		if (productType.equals("dxp")) {
			dirTypes.add("-dxp");
		}

		for (String dirType : dirTypes) {

			File docDir = new File("../" + _docDir);
			File articleDir = new File(docDir.getAbsolutePath() + "/articles" + dirType);
			System.out.println("Numbering images for files in "
					+ articleDir.getPath() + " ...");

			if (!articleDir.exists() || !articleDir.isDirectory()) {
				throw new BuildException("FAILURE - bad chapters directory " + articleDir);
			}

			File[] articleDirFiles = articleDir.listFiles();
			List<File> articles = new ArrayList<File>();

			Queue<File> q = new LinkedList<File>();
			for (File f : articleDirFiles) {
				q.add(f);
			}

			while (!q.isEmpty()) {
				File f = q.remove(); 

				if (f.isDirectory()) {
					File[] files = f.listFiles();

					for (File file : files) {
						q.add(file);
					}
				}
				else {
					if (f.getName().endsWith(".markdown")) {
						articles.add(f);
					}
				}
			}

			for (File article : articles) {
				String articlePath = article.getAbsolutePath();

				try {
					ResetImagesDiscover.resetImages(articlePath);
					NumberImagesDiscover.numberImages(articlePath);
				}
				catch (IOException ie) {
					throw new BuildException(ie.getLocalizedMessage());
				}
			}
		}
	}

	public void setDocDir(String docDir) {
		_docDir = docDir;
	}

	public void setProductType(String productType) {
		_productType = productType;
	}

	private String _docDir;
	private String _productType;
}
