/**
 * 
 */
package org.promasi.game;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.joda.time.DateTime;
import org.promasi.game.company.Company;
import org.promasi.game.company.ICompanyListener;
import org.promasi.game.company.MarketPlace;
import org.promasi.game.company.SerializableEmployeeTask;
import org.promasi.game.project.Project;
import org.promasi.utilities.exceptions.NullArgumentException;
import org.promasi.utilities.serialization.SerializationException;

/**
 * @author m1cRo
 *
 */
public class GameModel
{
	/**
	 * 
	 */
	protected String _gameName;
	
	/**
	 * 
	 */
	protected String _gameDescription;
	
	/**
	 * 
	 */
	protected MarketPlace _marketPlace;
	
	/**
	 * 
	 */
	protected Company _company;
	
	/**
	 * 
	 */
	protected Queue<Project> _projects;
	
	/**
	 * 
	 */
	protected Queue<Project> _runnedProjects;
	
	/**
	 * 
	 */
	private List<IGameModelListener> _listeners;
	
	/**
	 * 
	 */
	private boolean _gameFinished;
	
	/**
	 * 
	 * @param gameName
	 * @param gameDescription
	 * @param marketPlace
	 * @param company
	 * @param projects
	 * @throws IllegalArgumentException
	 * @throws NullArgumentException
	 */
	public GameModel(final String gameName, final String gameDescription, final MarketPlace marketPlace, final Company company,final Queue<Project> projects)throws GameException{
		if(company==null){
			throw new GameException("Wrong argument company==null");
		}

		if(marketPlace==null){
			throw new GameException("Wrong argument marketPlace==null");
		}
		
		if(projects==null){
			throw new GameException("Wrong argument projects==null");
		}
		
		if(gameName==null){
			throw new GameException("Wrong argument gameName==null");
		}
		
		if(gameDescription==null){
			throw new GameException("Wrong argument gameDescription==null");
		}
		
		if(projects.isEmpty()){
			throw new IllegalArgumentException("Wrong argument projects is empty");
		}
		
		_projects=projects;
		_marketPlace=marketPlace;
		_company=company;
		_listeners=new LinkedList<IGameModelListener>();
		_gameName=gameName;
		_gameDescription=gameDescription;
		_runnedProjects=new LinkedList<Project>();
		_gameFinished=false;
	}
	
	/**
	 * 
	 * @return
	 * @throws SerializationException
	 */
	public SerializableGameModel getSerializableGameModel()throws SerializationException{
		try {
			return new SerializableGameModel(this);
		} catch (NullArgumentException e) {
			throw new SerializationException("Serialization failed because " + e.getMessage());
		}
	}

	public void hireEmployee(String employeeId) throws NullArgumentException, IllegalArgumentException, SerializationException {
		if( !_marketPlace.hireEmployee(_company, employeeId) ){
			throw new IllegalArgumentException("Wrong argument employee");
		}
		
	}

	public void dischargeEmployee(String employeeId) throws NullArgumentException, IllegalArgumentException,SerializationException {
		if(!_company.dischargeEmployee(employeeId, _marketPlace)){
			throw new IllegalArgumentException("Wrong argument employee");
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getName(){
		return _gameName;
	}
	
	/**
	 * 
	 * @param employeeId
	 * @param employeeTasks
	 * @return
	 */
	public boolean assignTasks(String employeeId,List<SerializableEmployeeTask> employeeTasks) {
		try {
			return _company.assignTasks(employeeId, employeeTasks);
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getGameDescription() {
		return _gameDescription;
	}

	/**
	 * 
	 * @param listener
	 * @return
	 */
	public boolean addCompanyListener( ICompanyListener listener ){
		return _company.addListener(listener);
	}
	
	/**
	 * 
	 * @param listener
	 * @return
	 */
	public boolean removeCompanyListener( ICompanyListener listener ){
		return _company.removeListener(listener);
	}
	
	/**
	 * 
	 * @param listener
	 * @return
	 * @throws NullArgumentException
	 */
	public synchronized boolean removeGameModelListener(IGameModelListener listener) {
		if(listener==null){
			return false;
		}
		
		if(_listeners.contains(listener)){
			_listeners.remove(listener);
		}else{
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param listener
	 * @return
	 * @throws NullArgumentException
	 */
	public synchronized boolean addListener(IGameModelListener listener) {
		if(listener==null){
			return false;
		}
		
		if( !_listeners.contains(listener) ){
			_listeners.add(listener);
		}else{
			return false;
		}
		
		return true;
	}

	/**
	 * 
	 * @param currentDateTime
	 * @return
	 * @throws NullArgumentException
	 */
	public boolean executeGameStep(DateTime currentDateTime) {
		try{
			if( !_company.hasAssignedProject() && _projects.size()>0){
				Project project=_projects.poll();
				if( project!=null ){
					_company.assignProject(project);
					_runnedProjects.add(project);
				}
			}else if(_projects.size()==0 && !_company.hasAssignedProject() && !_gameFinished){
				for(IGameModelListener listener : _listeners){
					listener.gameFinished(this, _company.getSerializableCompany());
				}
				
				_gameFinished=true;
			}else{
				_company.executeWorkingStep(currentDateTime,_marketPlace);
			}
		}catch(SerializationException e){
			return false;
		}catch(NullArgumentException e){
			return false;
		}
		
		return true;
	}
}