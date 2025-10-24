import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../Auth/AuthContext'

export default function Header(){
  const { user, logout } = useAuth()
  const nav = useNavigate()

  function doLogout(){
    logout()
    nav('/')
  }

  return (
    <header className="header container">
      <div className="header-left" style={{display:'flex',alignItems:'center',gap:12}}>
        <Link to="/" className="home-button" aria-label="Ir al inicio">
          <span className="home-icon">🏠</span>
        </Link>
        <img src="/images/logo.png" alt="Fidexa" className="header-logo" onError={(e)=>e.target.style.display='none'} />
      </div>
      <nav className="header-nav">
        <button className="nav-button" onClick={()=>nav('/')}>Home</button>
        {!user && <button className="nav-button nav-cta" onClick={()=>nav('/register')}>Registro</button>}
        {user ? (
          <div className="nav-user">
            <span className="user-name">{user.fullName || user.username || user.email}</span>
            <button className="btn" onClick={doLogout}>Salir</button>
          </div>
        ) : (
          <button className="nav-button" onClick={()=>nav('/login')}>Ingresar</button>
        )}
      </nav>
    </header>
  )
}
