function reply_click()
{
    alert("www");
}
function myFunction()
{
    document.getElementById("50").style.backgroundColor = "red !important";
}
var isChecked = false;
var pieceChecked;
var pieceText = "";
var pieceID;
var jobj = "";

$(window).load(function () {
    
    $(".chessboard div").click(function(event) {    //evento click su un tassello di gioco
        event.preventDefault();
        if(isChecked == true)
        {
			if(this.id == pieceID)
			{
				$(this).removeClass("bg_yellow");
				for(var i = 0; i < jobj.nvalids; ++i)
				{
					var element = document.getElementById(jobj.valids[i]);
					$(element).removeClass("bg_green");
				}
				isChecked = false;
			}
			else if($(this).hasClass("bg_green"))
			{
					//invio la mossa al server
					//alert(this.id);
					//faccio il parsing del json
					//if(json.response == 0)
					pieceChecked.removeClass("white"); //DINAMICA
					pieceChecked.removeClass("bg_yellow");
					$(this).addClass("white"); //DINAMICA
					$(this).html(pieceText);
					$(pieceChecked).html("");
					for(var i = 0; i < jobj.nvalids; ++i)
					{
						var element = document.getElementById(jobj.valids[i]);
						$(element).removeClass("bg_green");
					}
					isChecked = false;
					/*if(json.enroque == 1)
					{
						var in = json.enroque
					}*/
					//else server error
			}
		}
		else if($(this).hasClass("white")) //DINAMICA
        {
			isChecked = true;
			pieceChecked = $(this);
			pieceID = this.id;
			pieceText = $(this).text();
			
			//QUERY AL SERVER, RICEVO UN JSON
			/*COORDINATE PER IL SERVER
				 * var coord = (this.id).split("",2);
				var let = coord[0];
				var num = coord[1];*/
			//if(response == 0)
			//non puoi muovere questa pedina
			//else
			$(this).addClass("bg_yellow");
			var v = '{"valids":["B3","G6"],"nvalids":2}';
			jobj = JSON.parse(v);
			
			for(var i = 0; i < jobj.nvalids; ++i)
			{
				var element = document.getElementById(jobj.valids[i]);
				$(element).addClass("bg_green");
				if(
			}
				
				
				

			
		}
        
		
		
        
        
        /*if(pedinaDragged == true )   //se sto trascinando una pedina la poggio 
        {
            if($(this).attr("id") != oldPedinaElement.attr("id")){ //... ovunque ma non sullo stesso posto
                //alert("voglio poggiare: " + artPedinaDragged);
                $(this).html(artPedinaDragged);
                oldPedinaElement.html("");
            }
            oldPedinaElement.removeClass("bg_green");
            pedinaDragged = false;
        }
        else    //altrimenti permetto di prendere sole pedoni bianchi
        {
            if($(this).hasClass("white"))   //check colore pedina
            {
                //alert("click su pedina bianca");
                if($( this ).hasClass("pedone"))    //check tipologia pedina
                {
                    pedinaDragged = true;
                    //alert("click su pedone");
                    tipoPedinaDragged = "pedone";
                    colorePedinaDragged = "white";
                    artPedinaDragged = $(this).text();
                    oldPedinaElement = $(this);
                    $(this).addClass("bg_green");
                }
            }
        }*/

    });
});
