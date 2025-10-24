import React, { useEffect, useState, useRef } from 'react'
import { Link } from 'react-router-dom'

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

  return (
    <main className="container">
      <section className="welcome-hero">
        <div className="welcome-content">
          <h2 className="hero-title">Bienvenido a Fidexa Bank</h2>
          <p className="lead">Digital, elegante y seguro. Gestiona tus cuentas y transacciones con facilidad.</p>
          <div className="hero-actions">
            <Link to="/register"><button className="btn btn-cta">Registrarse</button></Link>
            <Link to="/login"><button className="btn btn-ghost">Ingresar</button></Link>
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
