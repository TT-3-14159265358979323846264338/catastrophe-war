package com.example.catastrophewar.itemget;

import java.awt.Point;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;

import com.example.commonclass.Timer;
import com.example.defaultdata.Core;
import com.example.defaultdata.Gacha;
import com.example.defaultdata.Weapon;

public class ItemGetPageState  extends Timer{
	private ItemGetPage itemGetPage;
	private final String userName;
	private final String sessionId;
	private final SelectGacha selectGacha;
	private final AutoRotate autoRotate;
	private final HandleMotion handleMotion;
	private final FallBallMotion fallBallMotion;
	private final OpenBallMotion openBallMotion;
	
	public ItemGetPageState(ScheduledExecutorService scheduler, String userName, String sessionId){
		this.userName = userName;
		this.sessionId = sessionId;
		super(scheduler);
		selectGacha = createSelectGacha();
		autoRotate = createAutoRotate(scheduler);
		openBallMotion = createOpenBallMotion(scheduler);
		fallBallMotion = createfallBallMotion(scheduler);
		handleMotion = createHandleMotion(scheduler);
	}
	
	SelectGacha createSelectGacha(){
		return new SelectGacha();
	}
	
	AutoRotate createAutoRotate(ScheduledExecutorService scheduler) {
		return new AutoRotate(scheduler);
	}
	
	OpenBallMotion createOpenBallMotion(ScheduledExecutorService scheduler) {
		return new OpenBallMotion(this, scheduler);
	}
	
	FallBallMotion createfallBallMotion(ScheduledExecutorService scheduler) {
		return new FallBallMotion(openBallMotion, scheduler);
	}
	
	HandleMotion createHandleMotion(ScheduledExecutorService scheduler) {
		return new HandleMotion(this, fallBallMotion, scheduler);
	}
	
	GachaData gachaData() {
		return new GachaData(Gacha.values(), selectGacha.getGachaCountId(), selectGacha.getMedal());
	}
	
	record GachaData(Gacha[] gachaList, int gachaCountId, int medal) {}
	
	void drawTimerStart(Runnable task, int medalNumber, ItemGetPage itemGetPage) {
		this.itemGetPage = itemGetPage;
		selectGacha.setMedal(medalNumber);
		timerStart(task);
		autoRotate.timerStart();
	}
	
	State createState() {
		return new State(autoRotate.getAngle(),
				canPlayGacha(),
				handleMotion.getAngle(),
				getState(BallState::getTopPoint),
				getState(BallState::getBottomPoint),
				getState(BallState::getTopAngle),
				getState(BallState::getBottomAngle),
				openBallMotion.getColor(),
				openBallMotion.getExpansion());
	}
	
	boolean canPlayGacha() {
		return !isPlayingGacha() && selectGacha.canPlayGacha();
	}
	
	boolean isPlayingGacha() {
		return handleMotion.isRunning() || fallBallMotion.isRunning() || openBallMotion.isRunning();
	}
	
	<T> T getState(Function<BallState, T> task) {
		if(openBallMotion.isRunning()) {
			return task.apply(openBallMotion);
		}
		return task.apply(fallBallMotion);
	}
	
	record State(double turnAngle, 
			boolean canPlayGacha, 
			double handleAngle, 
			Point topPoint, 
			Point bottomPoint, 
			double topAngle, 
			double bottomAngle, 
			int color, 
			int expansion) {}
	
	void playGacha() {
		itemGetPage.playGacha(userName, sessionId);
	}
	
	void endGacha() {
		GachaResultCreating gachaResultCreating = gachaResult();
		selectGacha.setMedal(itemGetPage.endGacha(selectGacha.getUsedMedal(), gachaResultCreating.getResult(), userName, sessionId));
	}
	
	GachaResultCreating gachaResult() {
		return new GachaResultCreating(selectGacha.selectGachaData(), selectGacha.getGachaCountLabel());
	}
	
	void mousePressed(int x, int y) {
		if(selectGacha.canPlayGacha()) {
			handleMotion.mousePressed(x, y);
		}
	}
	
	void mouseDragged(int x, int y){
		if(selectGacha.canPlayGacha()) {
			handleMotion.mouseDragged(x, y);
		}
	}
	
	void mouseReleased() {
		handleMotion.mouseReleased();
	}
	
	void changeSelected(int selectId) {
		selectGacha.setSelectId(selectId);
	}
	
	Detail detail() {
		return new Detail(selectGacha.getCoreLineup(), selectGacha.getCoreRatio(), selectGacha.getWeaponLineup(), selectGacha.getWeaponRatio());
	}
	
	record Detail(List<Core> coreLineup, List<Double> coreRatio, List<Weapon> weaponLineup, List<Double> weaponRatio) {}
	
	void changeCount(int id) {
		selectGacha.setGachaCount(id);
	}
	
	@Override
	public void timerStop() {
		super.timerStop();
		autoRotate.timerStop();
	}
}