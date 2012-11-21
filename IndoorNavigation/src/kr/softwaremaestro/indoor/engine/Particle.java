package kr.softwaremaestro.indoor.engine;

public class Particle{
	private double x;
	private double y;
	private double weight;
	private double vx, vy;
	
	public Particle(double x, double y, double vx, double vy, double weight){	
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.weight = weight;
	}
	
	public Particle(double x, double y, double weight){
		this.x = x;
		this.y = y;
		this.weight = weight;
	}
	
	public Particle(double x, double y){
		this.x = x;
		this.y = y;
		this.weight = 0;
	}
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public double getWeight(){
		return weight;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public void setY(double y){
		this.y = y;
	}

	
	public void setWeight(double weight){
		this.weight = weight;
	}

	public void setVx(double vx) {
		this.vx = vx;
	}

	public void setVy(double vy) {
		this.vy = vy;
	}

	public double getVx() {
		return vx;
	}

	public double getVy() {
		return vy;
	}
}
