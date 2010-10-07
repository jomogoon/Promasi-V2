/**
 * 
 */
package org.promasi.multiplayer;

import java.io.Serializable;

import org.apache.commons.lang.NullArgumentException;
import org.promasi.model.Company;
import org.promasi.model.MarketPlace;

/**
 * @author m1cRo
 *
 */
public class GameStory implements Serializable
{
	/**
	 * 
	 */
	public static final String CONST_DEFAULT_GAME_DESCRIPTION="Game description";
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * 
	 */
	private Company _company;
	
	
	/**
	 * 
	 */
	private String _gameId;
	
	/**
	 * 
	 */
	private String _gameDescription;
	
	/**
	 * 
	 */
	private MarketPlace _marketPlace;
	
	/**
	 * 
	 */
	private int _numberOfPlayers;
	
	
	/**
	 * 
	 */
	public GameStory()
	{
		_marketPlace=new MarketPlace();
		_company=new Company();
		_numberOfPlayers=0;
		_gameDescription=new String("New game");
	}
	
	/**
	 * 
	 * @param company
	 * @param sdModel
	 * @param gameId
	 * @param numberOfPlayers
	 * @throws NullArgumentException
	 * @throws IllegalArgumentException
	 */
	public GameStory(Company company,String gameId,String gameDescription,int numberOfPlayers,MarketPlace marketPlace)throws NullArgumentException,IllegalArgumentException
	{
		if(company==null)
		{
			throw new NullArgumentException("Wrong argument company==null");
		}
		
		if(gameId==null)
		{
			throw new NullArgumentException("Wrong argument gameId==null");
		}
		
		if(gameDescription==null)
		{
			throw new NullArgumentException("Wrong argument gameDescription==null");
		}
		
		if(numberOfPlayers<0)
		{
			throw new NullArgumentException("Wrong argument numberOfPlayers<0");
		}
		
		if(marketPlace==null)
		{
			throw new NullArgumentException("Wrong argument employees==null");
		}
		
		_gameDescription=gameDescription;
		_company=company;
		_gameId=gameId;
		_numberOfPlayers=numberOfPlayers;
		_marketPlace=marketPlace;
	}
	
	/**
	 * 
	 * @param company
	 * @throws NullArgumentException
	 */
	public synchronized void setCompany(Company company)throws NullArgumentException
	{
		if(company==null)
		{
			throw new NullArgumentException("Wrong argument company==null");
		}
		
		_company=company;
	}
	
	/**
	 * 
	 * @return
	 */
	public synchronized Company getCompany()
	{
		return _company;
	}
	
	/**
	 * 
	 * @param gameId
	 * @throws NullArgumentException
	 */
	public synchronized void setGameId(String gameId)throws NullArgumentException
	{
		if(gameId==null)
		{
			throw new NullArgumentException("Wrong argument gameId==null");
		}
		
		_gameId=gameId;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public synchronized String getGameId()
	{
		return _gameId;
	}
	
	/**
	 * 
	 * @param numberOfPlayers
	 * @throws IllegalArgumentException
	 */
	public synchronized void setNunmberOfPlayers(int numberOfPlayers)throws IllegalArgumentException
	{
		if(numberOfPlayers<0)
		{
			throw new IllegalArgumentException("Wrong argument numberOfPlayers<0");
		}
		
		_numberOfPlayers=numberOfPlayers;
	}
	
	/**
	 * 
	 * @return
	 */
	public synchronized int getNumberOfPlayers()
	{
		return _numberOfPlayers;
	}
	
	/**
	 * 
	 * @param gameDescription
	 * @throws NullArgumentException
	 */
	public synchronized void setGameDescription(String gameDescription)throws NullArgumentException
	{
		if(gameDescription==null)
		{
			throw new NullArgumentException("Wrong argument gameDescription==null");
		}
		
		_gameDescription=gameDescription;
	}
	
	/**
	 * 
	 * @return
	 */
	public synchronized String getGameDescription()
	{
		return _gameDescription;
	}
	
	/**
	 * 
	 * @param marketPlace
	 * @throws NullArgumentException
	 */
	public synchronized void setMarketPlace(MarketPlace marketPlace)throws NullArgumentException
	{
		if(marketPlace==null)
		{
			throw new NullArgumentException("Wrong argument employees==null");
		}
		
		_marketPlace=marketPlace;
	}
	
	/**
	 * 
	 * @return
	 */
	public synchronized MarketPlace getMarketPlace()
	{
		return _marketPlace;
	}
}
