
console.log("Script loaded");

let currentTheme = getTheme();

// call the change theme method when window load.
document.addEventListener("DOMContentLoaded", () =>{
    changeTheme();
});



// change theme
function changeTheme(){
//set theme in html
changePageTheme(currentTheme,"");

//set the listener to change the theme button
const changeThemeButton = document.querySelector("#theme_change_button");


changeThemeButton.addEventListener("click", (event) => {
      // store current Theme
     let oldTheme = currentTheme;
    console.log("theme changed");
    
    if(currentTheme === "dark"){
        currentTheme="light";
    }else{
        currentTheme = "dark";
    }
    console.log(currentTheme);
    changePageTheme(currentTheme,oldTheme);
  

});

}

// set theme to local storage //keys and value pair
function setTheme(theme){
    localStorage.setItem("theme",theme);

}

// get theme from local storage
function getTheme(){
    let theme = localStorage.getItem("theme");
    // if no value in local storage  if(theme) return theme;else return "light";
    return theme ? theme: "light";
   

}

function changePageTheme(theme,oldTheme){
      //update in localStorage
      setTheme(currentTheme);
      //remove current theme
      if(oldTheme){
      document.querySelector("html").classList.remove(oldTheme);
      console.log("removed");
      }
      // set current theme
      document.querySelector("html").classList.add(theme);
      console.log("add");
      
     
      
}