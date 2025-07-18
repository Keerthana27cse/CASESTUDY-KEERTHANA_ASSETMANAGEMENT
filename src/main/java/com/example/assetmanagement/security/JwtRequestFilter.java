package com.example.assetmanagement.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired private JwtUtil jwtUtil;

	@Autowired private UserDetailsService userDetailsService;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		

		String path = request.getServletPath();
		if (path.equals("/auth/login") || path.equals("/error")||path.equals("/auth/register")) {
		    filterChain.doFilter(request, response);
		    return;
		}
	
	final String authHeader=request.getHeader("Authorization");
	
	String username=null;
	String jwtToken=null;
	
	
	if(authHeader!=null && authHeader.startsWith("Bearer "))
	{
		jwtToken =authHeader.substring(7);
		username=jwtUtil.extractUsername(jwtToken);
	}
	if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null)
	{
       var userDetails=userDetailsService.loadUserByUsername(username);       
       
       if(jwtUtil.validateToken(jwtToken,userDetails))
    	   {
    	     var authToken=new UsernamePasswordAuthenticationToken(
    	    		userDetails,null,userDetails.getAuthorities());
    	      
    	     authToken.setDetails(new WebAuthenticationDetails(request));
    	     SecurityContextHolder.getContext().setAuthentication(authToken);
    	   
    	   }
	}
	filterChain.doFilter(request, response);
	}
}
