package org.promasi.game.company;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.promasi.utilities.exceptions.NullArgumentException;
import org.promasi.utilities.serialization.SerializationException;


/**
 * 
 * Represents an employee.(Developer,Tester,Designer etc). All the fields of the
 * employee(experienced etc) have a range of 0.0-10.0
 * 
 * @author m1cRo
 * 
 */
public class Employee 
{	
	/**
     * The name of the person.
     */
    protected String _firstName;

    /**
     * The last name of the person.
     */
    protected String _lastName;

    /**
     * 
     */
    protected String _employeeId;
    
	/**
     * The salary of the employee.
     */
    protected double _salary;

    
    /**
     * The CV of the employee.
     */
    protected String _curriculumVitae;
    
    /**
     * 
     */
    protected Map<String, Double> _employeeSkills;
    
	/**
	 * 
	 */
	private IEmployeeListener _employeeListener;
    
    /**
     * 
     */
    private Map<Integer ,EmployeeTask> _employeeTasks;
    
    /**
     * 
     */
    private Lock _lockObject;
    
    /**
     * Initializes the object.
     */
    public Employee(String firstName, String lastName, String employeeId, String curriculumVitae, double salary,Map<String, Double> employeeSkills)throws NullArgumentException, IllegalArgumentException
   {
    	
    	if(firstName==null){
    		throw new NullArgumentException("Wrong argument firstName==null");
    	}
    	
        if(lastName==null){
        	throw new NullArgumentException("Wrong argument lastName==null");
        }
        
        if(employeeId==null){
        	throw new NullArgumentException("Wrong argument employeeId==null");
        }
        
        if(curriculumVitae==null){
        	throw new NullArgumentException("Wrong argument curriculumVitae==null");
        }

        if(employeeSkills==null){
        	throw new NullArgumentException("Wrong argument employeeSkills==null");
        }
        
        for(Map.Entry<String, Double> entry : employeeSkills.entrySet()){
        	if(entry.getKey()==null || entry.getValue()==null){
        		throw new IllegalArgumentException("Wrong argument employeeSkills contains null");
        	}
        }
        
        _firstName=firstName;
        _lastName=lastName;
        _employeeId=employeeId;
        _curriculumVitae=curriculumVitae;
        _salary=salary;
        _employeeSkills=employeeSkills;
        _lockObject = new ReentrantLock();
        _employeeTasks=new TreeMap<Integer, EmployeeTask>();
    }

    /**
     * @return The {@link #_salary}.
     */
    public double getSalary ( ){
        return _salary;
    }

    /**
     * @return The {@link #_curriculumVitae}.
     */
    public String getCurriculumVitae ( ){
        return _curriculumVitae;
    }
    
    /**
     * 
     * @return
     */
    public String getFirstName(){
    	return _firstName;
    }
    
    /**
     * 
     * @return
     */
    public String getLastName(){
    	return _lastName;
    }
    
    /**
     * 
     * @return
     */
    public String getEmployeeId(){
    	return _employeeId;
    }
    
	/**
	 * 
	 * @return
	 * @throws SerializationException
	 */
	public SerializableEmployee getSerializableEmployee()throws SerializationException{
		try {
			return new SerializableEmployee(this);
		} catch (NullArgumentException e) {
			throw new SerializationException("Serialization failed because " + e.getMessage());
		}
	}

    /**
     * 
     * @param employeeTask
     * @return
     * @throws SerializationException 
     */
    public boolean assignTasks(List<EmployeeTask> employeeTasks){
    	boolean result = false;
    	
    	try{
    		_lockObject.lock();
        	if(employeeTasks!=null){
            	for(EmployeeTask task : employeeTasks){
                	for(Map.Entry<Integer , EmployeeTask> entry: _employeeTasks.entrySet()){
                		if( entry.getValue().conflictsWithTask(task) ){
                			return false;
                		}
                	}
            	}
            	
            	List<EmployeeTask> tmp=new LinkedList<EmployeeTask>(employeeTasks);
            	for(EmployeeTask tmpTask : tmp){
            		for(EmployeeTask employeeTask : employeeTasks){
            			if(tmpTask!=employeeTask && tmpTask.conflictsWithTask(employeeTask)){
            				return false;
            			}
            		}
            	}

            	List<SerializableEmployeeTask> serializableTasks=new LinkedList<SerializableEmployeeTask>();
            	for(EmployeeTask task : employeeTasks){
            		_employeeTasks.put(task.getFirstStep(), task);
            		serializableTasks.add( task.getSerializableEmployeeTask() );
            	}
        		
            	if(_employeeListener!=null){
            		_employeeListener.taskAttached(getSerializableEmployee(), serializableTasks);
            	}
            	
            	result = true;
        	}
    	}catch( SerializationException e){
    		result = false;
    	}finally{
    		_lockObject.unlock();
    	}
	
		return result;
    }
    
    /**
     * 
     * @return
     */
    public boolean removeAllTasks(){
    	boolean result = false;
    	try{
    		_lockObject.lock();
        	for(Map.Entry<Integer , EmployeeTask> entry : _employeeTasks.entrySet()){
        		if(_employeeListener!=null){
        			_employeeListener.taskDetached( getSerializableEmployee(), entry.getValue().getSerializableEmployeeTask() );
        		}
        	}
        	
        	_employeeTasks.clear();
        	result = true;
    	}catch (SerializationException e){
    		result = false;
    	}finally{
    		_lockObject.unlock();
    	}

    	return result;
    }
    
    /**
     * 
     * @param employeeTask
     * @return
     * @throws NullArgumentException
     * @throws SerializationException 
     */
    public synchronized boolean removeEmployeeTask(EmployeeTask employeeTask)throws NullArgumentException, SerializationException{
    	if(employeeTask==null){
    		throw new NullArgumentException("Wrong argument employeeTask==null");
    	}
    	
    	if(_employeeTasks.containsKey(employeeTask.getFirstStep() ) ){
    		EmployeeTask task=_employeeTasks.get(employeeTask.getFirstStep() );
    		if(task==employeeTask){
    			_employeeTasks.remove(task.getFirstStep());
    			if(_employeeListener!=null){
    				_employeeListener.taskDetached(getSerializableEmployee(), task.getSerializableEmployeeTask());
    			}
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    /**
     * 
     * @return
     */
    public boolean  executeTasks(int currentStep){
    	boolean result = false;
    	
    	try{
    		_lockObject.lock();
        	if(!_employeeTasks.isEmpty()){
            	Map<Integer, EmployeeTask> employeeTasks=new TreeMap<Integer, EmployeeTask> ();
            	for(Map.Entry<Integer ,EmployeeTask> entry: _employeeTasks.entrySet()){
            		try{
            			entry.getValue().executeTask(_employeeSkills, currentStep);
            			if(entry.getValue().isValid(currentStep)){
            				employeeTasks.put(entry.getKey(), entry.getValue());
            			}
            			
            			result = true;
            		}catch(NullArgumentException e){
            			return false;
            		}
            	}
            	
            	_employeeTasks=employeeTasks;
        	}
        	
    	}finally{
    		_lockObject.unlock();
    	}

    	return result;
    }

    /**
     * 
     * @param employeeEventHandler
     */
    public void setListener(IEmployeeListener employeeEventHandler){
    	try{
    		_lockObject.lock();
        	_employeeListener=employeeEventHandler;
    	}finally{
    		_lockObject.unlock();
    	}
    }
}
