package nyu.hezzze.weiqi.server;

import java.util.Date;

import com.googlecode.objectify.annotation.Embed;

/**
 * This class represents the rank of a player and has formulas to update the
 * rank
 * 
 * @author hezzze
 * 
 */
@Embed
public class Rank {

	public static final double DEFAULT_RATING = 1500;
	public static final double DEFAULT_RD = 350;
	public static final double c = Math.sqrt((350 * 350 - 50 * 50) / 365);
	public static final double q = Math.log(10) / 400;

	double r;
	double RD;

	public Rank() {
		r = DEFAULT_RATING;
		RD = DEFAULT_RD;
	}

	void updateRD(Date dateOfLastGame) {
		Date now = new Date();
		long dayPassed = millisecondsToDays(now.getTime()
				- dateOfLastGame.getTime());
		RD = Math.min(Math.sqrt(RD * RD + c * c * dayPassed), 350);
	}

	private long millisecondsToDays(long l) {
		return (long) ((double) l / 1000 / 60 / 60 / 24);
	}

	void updateAfterGame(double RDj, double rj, double sj) {
		double dSquare = dSquare(RDj, rj);
		r = r + q / (1 / (RD * RD) + 1 / dSquare) * g(RDj) * (sj - E(RDj, rj));
		RD = Math.sqrt(1 / (1 / (RD * RD) + 1 / dSquare));
	}

	private double dSquare(double RDj, double rj) {
		return 1 / (q * q * g(RDj) * g(RDj) * E(RDj, rj) * (1 - E(RDj, rj)));
	}

	private double g(double RD) {
		return 1 / Math.sqrt(1 + 3 * q * q * RD * RD / Math.PI / Math.PI);
	}

	private double E(double RDj, double rj) {
		return 1 / (1 + Math.pow(10, -g(RDj) * (r - rj) / 400));
	}

	public double getRD() {
		return RD;
	}

	public double getRating() {
		return r;
	}

	@Override
	public String toString() {
		return String.format("[%d, %d]", (long) (r - 2 * RD),
				(long) (r + 2 * RD));
	}

}
