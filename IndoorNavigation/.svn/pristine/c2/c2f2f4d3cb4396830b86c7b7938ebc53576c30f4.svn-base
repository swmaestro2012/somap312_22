package kr.softwaremaestro.indoor.engine;

import java.util.Random;

import kr.softwaremaestro.indoor.wrm.vo.Point;

public class ParticleFilter {
	private Point currLocation; //현재 위치
	private double interval; 
	private double prev_conf;
	private Particle particle[];
	private int numParticle = 5000;
	final int threshold = numParticle * 2/3;
	
	private int count=1;
	double stdev_v = 0.1;
	double stdev_s = 2.5;
	
	public ParticleFilter(Point initial, double interval) {
		this.currLocation = new Point(-1, initial.getX(), initial.getY(), initial.getZ());
		
		this.interval = interval;
		
		particle = new Particle[numParticle];
		
		Random rand = new Random();
		for(int i=0; i<particle.length; i++)
			particle[i] = new Particle(initial.getX() + (rand.nextDouble()-0.5) * 30, initial.getY() + (rand.nextDouble()-0.5) * 30, rand.nextGaussian() * 1.5 / Math.sqrt(2), rand.nextGaussian() * 1.5 / Math.sqrt(2), 1.0 / particle.length);
		
		count = 1;
	}
	
	
	public Point correct(Point measured, double confidence) {	
		double e = 1; //제곱 계수 
		double w_measure = Math.pow(prev_conf, e) / (Math.pow(prev_conf, e) + Math.pow(confidence, e));
		double w_state = 1-w_measure;

		prev_conf = (confidence + count * prev_conf) / (count + 1);
		count++;
		
		double x = 0, y = 0;
		for(int i=0; i<particle.length; i++){
			x += particle[i].getX()*particle[i].getWeight();
			y += particle[i].getY()*particle[i].getWeight();
		}
	
		Point predicted = new Point (-1, x, y, measured.getZ());
		Point estimated = new Point (-1, w_state*x + w_measure*measured.getX(), w_state*y + w_measure*measured.getY(), measured.getZ());

		
		//Shift
		double xShift = estimated.getX() - predicted.getX();
		double yShift = estimated.getY() - predicted.getY();
		
		for(int i=0; i<particle.length; i++){
			particle[i].setX(particle[i].getX() + xShift);
			particle[i].setY(particle[i].getY() + yShift);
		}


		measurementUpdate(estimated);
		resampling();		
		prediction();	
		
		return estimated;
	}

	public void setLocation(Point location) {
		currLocation.setX(location.getX());
		currLocation.setY(location.getY());
		currLocation.setZ(location.getZ());
	}
	
	private void measurementUpdate(Point estimated){
		double sum = 0;
		for(int i=0; i<particle.length; i++){
			double e_t = Math.sqrt(Math.pow(particle[i].getX() - estimated.getX(), 2) + Math.pow(particle[i].getY() - estimated.getY(), 2));
			//double e_t = Math.sqrt(Math.pow(measured.getX() - (nearLinkFinder.getModifiedLocation(new Location(-1, particle[i].getX(), particle[i].getY(), measured.getZ(), null)).getX() - particle[i].getX()), 2) + 
			//							Math.pow(measured.getY() - (nearLinkFinder.getModifiedLocation(new Location(-1, particle[i].getX(), particle[i].getY(), measured.getZ(), null)).getY() - particle[i].getY()), 2));

			particle[i].setWeight(particle[i].getWeight() * getNormalProb(e_t) * e_t);
			sum += particle[i].getWeight();
		}
		
		for(int i=0; i<particle.length; i++)
			particle[i].setWeight(particle[i].getWeight() / sum);
	}
	
	private void resampling(){
		int samplecount[] = new int[numParticle];
		int newNumParticle = 0;
		double sum = 0;
		
		for(int i=0; i<numParticle; i++)
			sum += Math.pow(particle[i].getWeight(), 2);
		
		if(1.0/sum >= threshold)
			return;			
		
		for(int i=0; i<numParticle; i++){	
			samplecount[i] = (int)Math.round(numParticle * particle[i].getWeight());
			newNumParticle += samplecount[i];
		}
				
		Particle[] newParticle = new Particle[newNumParticle];
				
		for(int i=0, k=0; i<numParticle; i++)
			for(int j=0; j<samplecount[i] && k<newNumParticle; j++, k++)
				newParticle[k] = new Particle(particle[i].getX(), particle[i].getY(), particle[i].getVx(), particle[i].getVy(), 1.0/newNumParticle);
	
		numParticle = newNumParticle;
		particle = newParticle;
	}
	
	private void prediction(){
		Random rand = new Random();
		for(int i=0; i<particle.length; i++){	
			particle[i].setX(particle[i].getX() + particle[i].getVx() * interval + Math.pow(interval, 2) / 2 * rand.nextGaussian() * stdev_v / Math.sqrt(2));		
			particle[i].setY(particle[i].getY() + particle[i].getVy() * interval + Math.pow(interval, 2) / 2 * rand.nextGaussian() * stdev_v / Math.sqrt(2));
			particle[i].setVx(particle[i].getVx() + interval * rand.nextGaussian() * stdev_v / Math.sqrt(2));
			particle[i].setVy(particle[i].getVy() + interval * rand.nextGaussian() * stdev_v / Math.sqrt(2));
		}
	}
	
	private double getNormalProb(double x){
		return 1 / Math.sqrt(2 * Math.PI) / stdev_s * Math.exp(-0.5 * Math.pow(x / stdev_s, 2));
	}
}


