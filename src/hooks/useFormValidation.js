import { useState, useCallback } from 'react'

// useFormValidation: simple validation hook
// rules: { fieldName: (value) => errorMessage | null }
export default function useFormValidation(initialValues = {}, rules = {}){
  const [values, setValues] = useState(initialValues)
  const [errors, setErrors] = useState({})

  const validateField = useCallback((name, value) => {
    const rule = rules[name]
    if(!rule) return null
    try{
      const res = rule(value, values)
      return res || null
    }catch(e){
      return String(e) || 'Error'
    }
  }, [rules, values])

  const setField = useCallback((name, value) => {
    setValues(v => ({ ...v, [name]: value }))
    const err = validateField(name, value)
    setErrors(e => ({ ...e, [name]: err }))
  }, [validateField])

  const validateAll = useCallback(() => {
    const newErrors = {}
    Object.keys(rules).forEach(k => {
      const err = validateField(k, values[k])
      if(err) newErrors[k] = err
    })
    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }, [rules, validateField, values])

  return { values, errors, setField, validateField, validateAll, setValues, setErrors }
}
