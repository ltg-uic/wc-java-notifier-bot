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
	private int bb3;
	private int gb3;
	private int pr3;
	private int bb_avg;
	private int gb_avg;
	private int pr_avg;
	private int s_f;
	private int f_f;
	private int bb_f;
	private int gb_f;
	private int pr_f;
	
	
	public CountRequestData(int wall, int light, int temp, int humid, int s,
			int f, int bb1, int gb1, int pr1, int bb2, int gb2, int pr2, 
			int bb3, int gb3, int pr3, int bb_avg, int gb_avg, int pr_avg,
			int s_f, int f_f, int bb_f, int gb_f, int pr_f) {
		super();
		this.wall = wall;
		this.light = light;
		this.temp = temp;
		this.humid = humid;
		this.s = s;
		this.f = f;
		this.bb1 = bb1;
		this.gb1 = gb1;
		this.pr1 = pr1;
		this.bb2 = bb1;
		this.gb2 = gb1;
		this.pr2 = pr1;
		this.bb3 = bb3;
		this.gb3 = gb3;
		this.pr3 = pr3;
		this.bb_avg = bb_avg;
		this.gb_avg = gb_avg;
		this.pr_avg = pr_avg;
		this.s_f = s_f;
		this.f_f = f_f;
		this.bb_f = bb_f;
		this.gb_f = gb_f;
		this.pr_f = pr_f;
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


	public int getBb3() {
		return bb3;
	}


	public int getGb3() {
		return gb3;
	}


	public int getPr3() {
		return pr3;
	}


	public int getBb_avg() {
		return bb_avg;
	}


	public int getGb_avg() {
		return gb_avg;
	}


	public int getPr_avg() {
		return pr_avg;
	}


	public int getS_f() {
		return s_f;
	}


	public int getF_f() {
		return f_f;
	}


	public int getBb_f() {
		return bb_f;
	}


	public int getGb_f() {
		return gb_f;
	}


	public int getPr_f() {
		return pr_f;
	}
}
