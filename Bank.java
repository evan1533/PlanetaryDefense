import java.util.*;

public class Bank
{
	private int balance = 0;
	PlanetaryDefense game;

	public Bank(int initialBalance, PlanetaryDefense pd)
	{
		balance = initialBalance;
		game = pd;
	}

	public int getBalance()
	{
		return this.balance;
	}

	public void deposit(int amount)
	{
		balance+=amount;
	}

	public void withdraw(int amount)
	{
		balance-=amount;
	}

	public void buy(Tower t)
	{
		if(balance>=t.cost)
		{
			this.balance-=t.cost;
			//System.out.println(t.cost);
		}
	}

	public void sell(Tower t)
	{
		this.balance+=t.sellValue;
	}

	public boolean canBuy(Tower t)
	{
		if(balance>=t.cost)
			return true;

		return false;
	}
}