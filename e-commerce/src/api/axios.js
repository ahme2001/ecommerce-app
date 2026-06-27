import axios from "axios";

const instance = axios.create({
    baseURL: import.meta.env.VITE_API_URL + "/api",
    headers : {
        "Content-Type": "application/json",
        "Authorization": `Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTc4MjU5MDkxNSwiZXhwIjoxNzg1NTkwOTE1fQ._EWlPbH2adi_KkDBRBt40cO9OD3OMMOqh6AiAIdTUGM`
    }
});

export default instance;