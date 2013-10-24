package nyu.hezzze.weiqi.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * The cell object that is responsible for rendering the information of a game
 * info object in a cell containing widget
 * 
 * @author hezzze
 * 
 */
public class GameCell extends AbstractCell<GameInfo> {

	private final ImageResource blackImgRes;
	private final ImageResource whiteImgRes;

	private static final String TD_STYLE = "style='padding: 0px 5px; border: 2px solid #000;'";

	private static final String RENDER_TEMPLATE = "<table style='border-collapse: collapse; text-align: center;'>"
			+ "<tbody>" + "<tr>" + "<td colspan='3'"
			+ TD_STYLE
			+ ">{0}</td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td rowspan='2'"
			+ TD_STYLE
			+ "><div>{1}</div>"
			+ "</td>"
			+ "<td "
			+ TD_STYLE
			+ "><div style='width: 80px;'>Whose Turn</div></td>"
			+ "<td "
			+ TD_STYLE
			+ " ><div style='width: 80px;'>{2}</div></td>"
			+ "</tr>"
			+ "<tr>"
			+ "<td "
			+ TD_STYLE
			+ ">Winner</td>"
			+ "<td "
			+ TD_STYLE
			+ ">{3}</td>" + "</tr>" + "</tbody>" + "</table>";

	private Templates templates = GWT.create(Templates.class);

	interface Templates extends SafeHtmlTemplates {
		@SafeHtmlTemplates.Template(RENDER_TEMPLATE)
		SafeHtml cell(String gameId, SafeHtml imageHtml, String whoseTurn,
				String winner);
	}

	public GameCell(ImageResource black, ImageResource white) {
		this.blackImgRes = black;
		this.whiteImgRes = white;
	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			GameInfo gameInfo, SafeHtmlBuilder sb) {
		if (gameInfo == null) {
			return;
		}

		SafeHtml rendered = templates.cell(gameInfo.getId(), gameInfo
				.getIAmPlaying().equals("B") ? getImageHtml(blackImgRes)
				: getImageHtml(whiteImgRes), gameInfo.getWhoseTurn(), gameInfo
				.getWinner() == null ? "" : gameInfo.getWinner());
		sb.append(rendered);

	}

	private SafeHtml getImageHtml(ImageResource image) {
		SafeHtml imageHtml = AbstractImagePrototype.create(image).getSafeHtml();
		return imageHtml;
	}

}
