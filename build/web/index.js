/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var mainTExy = document.getElementById("mainText");
var mainTExy = document.getElementById("submitBtn");

function submitClick(){
    
    var firebaseRef = firebase.database().ref();
    firebaseRef.child("Text").set("Some value2");
}