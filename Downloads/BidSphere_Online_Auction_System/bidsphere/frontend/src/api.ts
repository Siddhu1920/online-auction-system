import axios from 'axios';
export const api=axios.create({baseURL:import.meta.env.VITE_API_URL||'http://localhost:8080/api/v1'});
api.interceptors.request.use(c=>{const t=localStorage.getItem('bidsphere_token');if(t)c.headers.Authorization=`Bearer ${t}`;return c;});
export type Auction={id:number;title:string;description:string;category:string;conditionLabel:string;startingPrice:string;currentPrice:string;minIncrement:string;startTime:string;endTime:string;status:string;seller:{id:number;name:string};winner?:{name:string}};
