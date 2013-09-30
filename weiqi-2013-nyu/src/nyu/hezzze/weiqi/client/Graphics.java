package nyu.hezzze.weiqi.client;


import static nyu.hezzze.weiqi.shared.Gamer.BLACK;
import static nyu.hezzze.weiqi.shared.Gamer.WHITE;
import nyu.hezzze.weiqi.shared.Gamer;
import nyu.hezzze.weiqi.shared.GoBoard;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class Graphics extends Composite implements Presenter.View { 

	interface GoUiBinder extends UiBinder<Widget, Graphics> {};
	private static GoUiBinder uiBinder = GWT.create(GoUiBinder.class);
	
	@UiField Grid goBoard;
	
	private final Presenter presenter;
	private final GoImages goImages;
	
	public Graphics() {
		presenter = new Presenter(this);
		this.goImages = GWT.create(GoImages.class);
		initWidget(uiBinder.createAndBindUi(this));
		
		goBoard.resize(GoBoard.ROWS, GoBoard.COLS);
		goBoard.setWidth("570px");
	    goBoard.setHeight("570px");
	    //gameGrid.setStyleName("board");
		
		
		for (int i = 0; i < GoBoard.ROWS; i++) {
			for (int j = 0; j < GoBoard.COLS; j++) {
				Image blank = new Image();
				final int row = i;
				final int col = j;
				blank.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						presenter.makeMove(row, col);
					}
					
				});
				blank.setResource(goImages.blank());
				blank.setWidth("30px");
				blank.setHeight("30px");
				goBoard.setWidget(i, j, blank);
			}
		}
	}
	
	
	public Presenter getPresenter() {
		return presenter;
	}



	@Override
	public void setCell(int row, int col, Gamer gamer) {
		Image img = (Image) goBoard.getWidget(row, col);
		
		if(gamer == BLACK) {
			img.setResource(goImages.blackStone());
		} else if (gamer == WHITE) {
			img.setResource(goImages.whiteStone());
		} else {
			img.setResource(goImages.blank());
		}
		
	}
	
	
	
	
	
}
