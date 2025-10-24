import React, { useEffect, useState, useRef } from 'react'
import { useNavigate } from 'react-router-dom'

export default function WelcomePage(){
  const images = [
    '/images/bank_index.png',
    '/images/bank2_index.png',
    '/images/bank3_index.png'
  ]
  const [idx, setIdx] = useState(0)
  const mounted = useRef(false)

  useEffect(()=>{
    mounted.current = true
    const t = setInterval(()=>{
      setIdx(i => (i+1) % images.length)
    }, 3500)
    return ()=>{ mounted.current = false; clearInterval(t) }
  },[])
  const nav = useNavigate()

  return (
    <main className="container">
      <section className="welcome-hero">
        <div className="welcome-content">
          <h2 className="hero-title">Bienvenido a Fidexa Bank</h2>
          <p className="lead">Digital, elegante y seguro. Gestiona tus cuentas y transacciones con facilidad.</p>
          <div className="hero-actions">
            <button className="btn btn-cta" onClick={()=>nav('/register')}>Registrarse</button>
            <button className="btn btn-ghost" onClick={()=>nav('/login')}>Ingresar</button>
          </div>
        </div>

        <div className="welcome-graphic" aria-hidden="true">
          <div className="slider-imagenes">
            {images.map((src, i) => (
              <img
                key={src}
                src={src}
                alt={`Fidexa ${i+1}`}
                className={`slide ${i === idx ? 'active' : ''}`}
                onError={(e)=>e.target.remove()}
              />
            ))}
          </div>
          <div className="slider-dots">
            {images.map((_, i) => (
              <button key={i} className={`dot ${i===idx? 'dot-active':''}`} onClick={()=>setIdx(i)} aria-label={`Ir a imagen ${i+1}`}/>
            ))}
          </div>
        </div>
      </section>
    </main>
  )
}

