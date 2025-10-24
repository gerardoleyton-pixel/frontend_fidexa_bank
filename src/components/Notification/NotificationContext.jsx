import React, { createContext, useState, useContext } from 'react'

const NotificationContext = createContext()

export function NotificationProvider({ children }) {
  const [message, setMessage] = useState(null)
  const [type, setType] = useState('')

  function notify(msg, t = 'success', timeout = 4000) {
    setMessage(msg)
    setType(t)
    if (timeout) setTimeout(() => setMessage(null), timeout)
  }

  return (
    <NotificationContext.Provider value={{ message, type, notify }}>
      {children}
      {message && (
        <div style={{position:'fixed',right:20,top:20,zIndex:999}}>
          <div className={`alert ${type === 'error' ? 'error' : 'success'}`}>{message}</div>
        </div>
      )}
    </NotificationContext.Provider>
  )
}

export function useNotification(){
  return useContext(NotificationContext)
}
