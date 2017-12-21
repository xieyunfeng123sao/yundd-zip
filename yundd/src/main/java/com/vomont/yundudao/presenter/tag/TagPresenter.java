package com.vomont.yundudao.presenter.tag;

import com.vomont.yundudao.bean.TagBean;
import com.vomont.yundudao.model.OnHttpListener;
import com.vomont.yundudao.model.tag.TagModel;
import com.vomont.yundudao.model.tag.TagModelImpl;
import com.vomont.yundudao.mvpview.tag.ITagView;

public class TagPresenter {

	private TagModel tagModel;

	private ITagView iTagView;

	public TagPresenter(ITagView iTagView) {

		this.iTagView = iTagView;
		tagModel = new TagModelImpl();
	}

	public void getTag(String userid) {
		tagModel.getTag(userid, new OnHttpListener() {

			@Override
			public void onSucess(Object object) {
				iTagView.getTag((TagBean) object);
			}

			@Override
			public void onFail() {

			}
		});

	}

}
