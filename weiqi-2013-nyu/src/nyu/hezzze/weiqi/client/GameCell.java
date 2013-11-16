package nyu.hezzze.weiqi.client;

import nyu.hezzze.weiqi.shared.Gamer;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.resources.client.ImageResource;
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

	private final String HTML_TEMPLATE;
	private GoMessages goMessages;

	public GameCell(Graphics graphics) {
		this.blackImgRes = graphics.goResources.blackPlayer();
		this.whiteImgRes = graphics.goResources.whitePlayer();
		goMessages = graphics.goMessages;
		HTML_TEMPLATE = "<table style='border-collapse: collapse; text-align: center;'>"
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
				+ "><div style='width: 80px;'>"
				+ goMessages.whoseTurn()
				+ "</div></td>"
				+ "<td "
				+ TD_STYLE
				+ " ><div style='width: 80px;'>{2}</div></td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td "
				+ TD_STYLE
				+ ">"
				+ goMessages.winner()
				+ "</td>"
				+ "<td "
				+ TD_STYLE
				+ ">{3}</td>"
				+ "</tr>"
				+ "<tr>"
				+ "<td colspan='3'"
				+ TD_STYLE
				+ ">{4}</td>"
				+ "</tr>"
				+ "</tbody>" + "</table>";

	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			GameInfo gameInfo, SafeHtmlBuilder sb) {
		if (gameInfo == null) {
			return;
		}

		SafeHtml rendered = cell(
				gameInfo.getId(),
				gameInfo.getIAmPlaying() == Gamer.BLACK ? getImageHtml(blackImgRes)
						: getImageHtml(whiteImgRes),
				gameInfo.isMyTurn() ? goMessages.myTurn() : goMessages
						.othersTurn(), gameInfo.getWinnerName(),
				goMessages.startDate(gameInfo.getStartDate()));
		sb.append(rendered);

	}

	private SafeHtml cell(String gameId, String imageHtml, String whoseTurn,
			String winner, String startDate) {
		SafeHtmlBuilder bd = new SafeHtmlBuilder();
		String html = HTML_TEMPLATE;
		html = html.replaceAll("\\{0\\}", gameId);
		html = html.replaceAll("\\{1\\}", imageHtml);
		html = html.replaceAll("\\{2\\}", whoseTurn);
		html = html.replaceAll("\\{3\\}", winner);
		html = html.replaceAll("\\{4\\}", startDate);

		bd.appendHtmlConstant(html);
		return bd.toSafeHtml();

	}

	private String getImageHtml(ImageResource image) {
		return AbstractImagePrototype.create(image).getHTML();
	}

}
