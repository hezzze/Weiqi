package nyu.hezzze.weiqi.client;

import nyu.hezzze.weiqi.shared.State;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.media.client.Audio;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;

/**
 * Animates putting down a stone on the board
 * @author hezzze
 *
 */
public class SetStoneAnimation extends Animation {

	static final int STONE_SIZE = 30;
	AbsolutePanel gamePanel;
	ImageResource stoneRes;
	Image growing;
	Presenter presenter;
	Audio stoneSound;
	int cellCenterX;
	int cellCenterY;

	public SetStoneAnimation(Image cell, ImageResource stoneRes, Presenter presenter, Audio stoneSound) {
		gamePanel = (AbsolutePanel) (((Grid) cell.getParent()).getParent());
		growing = new Image(stoneRes);
		this.stoneRes = stoneRes;
		this.presenter = presenter;
		this.stoneSound = stoneSound;
		double width = 0;
		double height = 0;
		growing.setPixelSize((int) width, (int) height);

		cellCenterX = cell.getAbsoluteLeft()
				+ (int) ((double) cell.getWidth() / 2)
				- gamePanel.getAbsoluteLeft();
		cellCenterY = cell.getAbsoluteTop()
				+ (int) ((double) cell.getHeight() / 2)
				- gamePanel.getAbsoluteTop();

		gamePanel.add(growing, (int) (cellCenterX - width / 2),
				(int) (cellCenterY - height / 2));

	}

	@Override
	protected void onUpdate(double progress) {
		double width = progress * STONE_SIZE;
		double height = progress * STONE_SIZE;

		gamePanel.remove(growing);
		growing = new Image(stoneRes);
		growing.setPixelSize((int) width, (int) height);

		gamePanel.add(growing, (int) (cellCenterX - width / 2),
				(int) (cellCenterY - height / 2));
	}
	
	@Override
	protected void onStart() {
		if (stoneSound!=null) {
			stoneSound.play();
		}
	}

	@Override
	protected void onComplete() {
		gamePanel.remove(growing);
		
		History.newItem("state="
				+ State.serialize(presenter.currentState));
	}

}
