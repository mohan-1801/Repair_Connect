import React,{createContext,useContext,useState,useEffect} from 'react'; import api from '../services/api';
const AuthContext=createContext(null);
export function AuthProvider({children}){
  const [user,setUser]=useState(null);
  const [loading,setLoading]=useState(true);
  useEffect(()=>{
    const token=localStorage.getItem('token');
    const u=localStorage.getItem('user');
    if(token&&u){
      try{
        setUser(JSON.parse(u));
      }catch(e){
        localStorage.removeItem('token');
        localStorage.removeItem('user');
      }
    }
    setLoading(false);
  },[]);
  async function login(data, expectedRole){
    const r=await api.post('/auth/login',data);
    if(expectedRole && r.data.user.role !== expectedRole){
      throw { response: { status: 403, data: { message: `Account is not registered as ${expectedRole === 'SERVICE_PROVIDER' ? 'a Service Provider' : expectedRole.toLowerCase() === 'admin' ? 'an Admin' : 'a Customer'}.` } } };
    }
    localStorage.setItem('token',r.data.token);
    localStorage.setItem('user',JSON.stringify(r.data.user));
    setUser(r.data.user);
    return r.data.user;
  }
  function logout(){
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
    window.location.href='/login';
  }
  return <AuthContext.Provider value={{user,loading,login,logout}}>{children}</AuthContext.Provider>
}
export const useAuth=()=>useContext(AuthContext);