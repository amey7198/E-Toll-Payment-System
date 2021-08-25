package com.cdac.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.model.Login;
import com.cdac.model.SourceDestination;
import com.cdac.model.Toll_Details;
import com.cdac.model.User;
import com.cdac.model.UsersVehicle;
import com.cdac.model.VehicleVerify;
import com.cdac.model.WalletAccount;
import com.cdac.service.InsertVehicleService;
import com.cdac.service.Services;
import com.cdac.service.ValidateVehicleService;
import com.cdac.service.WalletService;


@RestController
@CrossOrigin
public class UserController 
{
	String vehicleType;
	
	String loginId;
	
	long account_num;
	
	@Autowired
	private Services userServices;
	
	@Autowired
	private ValidateVehicleService validateVehicleService;
	
	@Autowired
	InsertVehicleService insertVehicleService;
	
	@Autowired
	WalletService walletService;
	
	
	@PostMapping(value="/register" ,produces="application/json")
	public String registerUser(@RequestBody User user)
	{
		System.out.println(user.getUser_loginId());
		return userServices.registerService(user);
		
	}
	
	@Autowired
	HttpSession session;
	
	@PostMapping(value="/login",produces="application/json")
	public String loginUser(@RequestBody Login login) throws Exception
	{
		System.out.println(login.getLoginId()+"===================================================");
		System.out.println(login.getPassword());
		session.setAttribute("LoginId",login.getLoginId());
		loginId=login.getLoginId();
		
		String val=userServices.loginService(login);
		if(val=="Successful")
		{
			session.setAttribute("LoginId", login.getLoginId());
		}
		return val;
			
	}
	
	
	
	@PostMapping(value="/validate",produces="application/json")
	public String validateVehicle(@RequestBody VehicleVerify vehicleVerify)
	{
		System.out.println(vehicleVerify);
			String ret=validateVehicleService.validateVehicle(vehicleVerify);
			if(ret=="Vehicle type matched")
			{
				vehicleType=vehicleVerify.getVehicle_type();
				session.setAttribute("RegNo", vehicleVerify.getVehicle_regNo());
			
			}
			return ret;
			
			
	}
	
	@PostMapping(value="/fetchTolls",produces="application/json")
	public ArrayList<Toll_Details> fetchTolls(@RequestBody SourceDestination sourceDestination)
	{
		System.out.println(vehicleType);
		return validateVehicleService.fetchTolls(sourceDestination,vehicleType);
		
	}
	
//	@PostMapping(value="/wallet",produces="application/json")
//	public void addToWallet()
//	{
//		User user=new User();
//		user.setUser_loginId(loginId);
//		account_num=walletAcc.getAccount_no();
//		walletAcc.setUser(user);
//		walletService.addRetrieve(loginId);
//		
//		
//	}
	
	@PostMapping(value="/pay",produces="application/json")
	public int payMoney(@RequestBody Integer amt)
	{
		System.out.println(amt);
		
		int rem_bal=walletService.payToll(loginId, amt);
		return rem_bal;
		
	}
			
		
}
