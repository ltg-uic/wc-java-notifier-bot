/*
 * Created Mar 13, 2012
 */
package ltg.es.wallcology.notifier.requests;

/**
 * TODO Description
 *
 * @author Gugo
 */
public class CountRequestData extends RequestData {
	
	// Count data
	private String origin;
	private int wall;
	private int light;
	private int temp;
	private int humid;
	private int s;
	private int f;
	private int bb1;
	private int gb1;
	private int pr1;
	private int bb2;
	private int gb2;
	private int pr2;
	private double bb_avg;
	private double gb_avg;
	private double pr_avg;
	private int s_f;
	private int f_f;
	private double bb_f;
	private double gb_f;
	private double pr_f;
	private int s_mult;
	private int f_mult;
	private int bb_mult;
	private int gb_mult;
	private int pr_mult;
	
	
	public CountRequestData(String orig, int wall, int light, int temp, int humid, int s,
			int f, int bb1, int gb1, int pr1, int bb2, int gb2, int pr2, 
			double bb_avg, double gb_avg, double pr_avg,
			int s_f, int f_f, double bb_f, double gb_f, double pr_f,
			int s_mult, int f_mult, int bb_mult, int gb_mult, int pr_mult) {
		super();
		this.origin = orig;
		this.wall = wall;
		this.light = light;
		this.temp = temp;
		this.humid = humid;
		this.s = s;
		this.f = f;
		this.bb1 = bb1;
		this.gb1 = gb1;
		this.pr1 = pr1;
		this.bb2 = bb2;
		this.gb2 = gb2;
		this.pr2 = pr2;
		this.bb_avg = bb_avg;
		this.gb_avg = gb_avg;
		this.pr_avg = pr_avg;
		this.s_f = s_f;
		this.f_f = f_f;
		this.bb_f = bb_f;
		this.gb_f = gb_f;
		this.pr_f = pr_f;
		this.s_mult = s_mult;
		this.f_mult = f_mult;
		this.bb_mult = bb_mult;
		this.gb_mult = gb_mult;
		this.pr_mult = pr_mult;
	}


	public String getOrigin() {
		return origin;
	}
	
	
	public int getWall() {
		return wall;
	}


	public int getLight() {
		return light;
	}


	public int getTemp() {
		return temp;
	}


	public int getHumid() {
		return humid;
	}


	public int getS() {
		return s;
	}


	public int getF() {
		return f;
	}


	public int getBb1() {
		return bb1;
	}


	public int getGb1() {
		return gb1;
	}


	public int getPr1() {
		return pr1;
	}


	public int getBb2() {
		return bb2;
	}


	public int getGb2() {
		return gb2;
	}


	public int getPr2() {
		return pr2;
	}


	public double getBb_avg() {
		return bb_avg;
	}


	public double getGb_avg() {
		return gb_avg;
	}


	public double getPr_avg() {
		return pr_avg;
	}


	public int getS_f() {
		return s_f;
	}


	public int getF_f() {
		return f_f;
	}


	public double getBb_f() {
		return bb_f;
	}


	public double getGb_f() {
		return gb_f;
	}


	public double getPr_f() {
		return pr_f;
	}


	public int getS_mult() {
		return s_mult;
	}


	public int getF_mult() {
		return f_mult;
	}


	public int getBb_mult() {
		return bb_mult;
	}


	public int getGb_mult() {
		return gb_mult;
	}


	public int getPr_mult() {
		return pr_mult;
	}
	
}
