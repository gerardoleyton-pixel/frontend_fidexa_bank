import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function AdminLoginPage(){
  const [user,setUser] = useState('')
  const [pass,setPass] = useState('')
  const [err,setErr] = useState('')
  const nav = useNavigate()

  function submit(e){
    e.preventDefault()
    if(user==='admin' && pass==='admin123'){
      localStorage.setItem('fidexa_admin','true')
      nav('/admin')
    }else{
      setErr('Credenciales admin inválidas')
    }
  }

  return (
    <main className="container">
      <h2>Admin Login</h2>
      <form className="form" onSubmit={submit}>
        <div className="form-row"><label>Usuario</label><input className="input" value={user} onChange={e=>setUser(e.target.value)} /></div>
        <div className="form-row"><label>Contraseña</label><input className="input" type="password" value={pass} onChange={e=>setPass(e.target.value)} /></div>
        {err && <div className="alert error">{err}</div>}
        <div style={{display:'flex',gap:8}}><button className="btn" type="submit">Ingresar Admin</button></div>
      </form>
    </main>
  )
}
