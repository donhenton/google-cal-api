
import HorizontalDemo from './demos/horizontalDemo';

var hDemo = null;



$(document).ready(
    function () {
        let hDemo = new HorizontalDemo();
        let updateH = hDemo.updateData.bind(hDemo);
        $('#updateHorizontalData').on('click',updateH);
   
       
       

         
         
    });
