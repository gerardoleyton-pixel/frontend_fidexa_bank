import React, { createContext, useState, useContext, useEffect } from 'react'
import authService from '../../services/auth'

const AuthContext = createContext()

export function AuthProvider({ children }){
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(false)

  useEffect(()=>{
    // restore from localStorage
    const raw = localStorage.getItem('fidexa_user')
    if(raw){
      try{setUser(JSON.parse(raw))}catch(e){/* ignore */}
    }
  },[])

  async function login(email, password){
    setLoading(true)
    try{
      const u = await authService.login({email,password})
      setUser(u)
      localStorage.setItem('fidexa_user', JSON.stringify(u))
      return u
    }finally{setLoading(false)}
  }

  function updateUser(updated){
    setUser(updated)
    try{ localStorage.setItem('fidexa_user', JSON.stringify(updated)) }catch(e){}
  }

  function logout(){
    setUser(null)
    localStorage.removeItem('fidexa_user')
  }

  return (
    <AuthContext.Provider value={{user, login, logout, loading, updateUser}}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth(){
  return useContext(AuthContext)
}
