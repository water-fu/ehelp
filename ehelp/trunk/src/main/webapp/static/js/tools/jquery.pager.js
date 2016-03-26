/*
* jQuery pager plugin
* Version 1.0 (12/22/2008)
* @requires jQuery v1.2.6 or later
*
* Example at: http://jonpauldavies.github.com/JQuery/Pager/PagerDemo.html
*
* Copyright (c) 2008-2009 Jon Paul Davies
* Dual licensed under the MIT and GPL licenses:
* http://www.opensource.org/licenses/mit-license.php
* http://www.gnu.org/licenses/gpl.html
* 
* Read the related blog post and contact the author at http://www.j-dee.com/2008/12/22/jquery-pager-plugin/
*
* This version is far from perfect and doesn't manage it's own state, therefore contributions are more than welcome!
*
* Usage: .pager({ pagenumber: 1, pagecount: 15, buttonClickCallback: PagerClickTest });
*
* Where pagenumber is the visible page number
*       pagecount is the total number of pages to display
*       buttonClickCallback is the method to fire when a pager button is clicked.
*
* buttonClickCallback signiture is PagerClickTest = function(pageclickednumber) 
* Where pageclickednumber is the number of the page clicked in the control.
*
* The included Pager.CSS file is a dependancy but can obviously tweaked to your wishes
* Tested in IE6 IE7 Firefox & Safari. Any browser strangeness, please report.
*/
(function($) {

    $.fn.pager = function(options) {

        var opts = $.extend({}, $.fn.pager.defaults, options);

        return this.each(function() {

        // empty out the destination element and then render out the pager with the supplied options
            $(this).empty().append(renderpager(parseInt(options.pagenumber), parseInt(options.pagecount), options.buttonClickCallback));
            
        });
    };
    
      function getInterval(pagenumber,pagecount)  {
		var start = 1;
		var end = 10;
		//与pagecount的差距
		var pcLen = pagecount - pagenumber;
		//与第一页差距
		var pfLen = pagenumber - 1;
		if(pcLen <= 5){
			end = pagecount;
			start = pagecount - 8;
		}
		if(pfLen <= 5){
			start = 1;
			end = 8;
		}
		if(pcLen <=5 && pfLen > 5){
			start = pagecount - 7;
			end = pagecount;
		}
		if(pcLen > 5 && pfLen > 5){
			start = pagenumber - 3;
			end = pagenumber + 2;
		}
		return {start:start, end:end};
	}

    // render and return the pager with the supplied options
    function renderpager(pagenumber, pagecount, buttonClickCallback) {

        // setup $pager to hold render
        var $pager = $('<ul class="pages"></ul>');

        // add in the previous and next buttons .append(renderButton('上一页', pagenumber, pagecount, buttonClickCallback))
        $pager.append(renderButton('首页', pagenumber, pagecount, buttonClickCallback)).append(renderButton('上一页', pagenumber, pagecount, buttonClickCallback));

        // pager currently only handles 10 viewable pages ( could be easily parameterized, maybe in next version ) so handle edge cases
        var startPoint = 1;
        var endPoint = pagecount;

/*        if (pagenumber > 4) {
            startPoint = pagenumber - 4;
            endPoint = pagenumber + 4;
        }

        if (endPoint > pagecount) {
            startPoint = pagecount - 8;
            endPoint = pagecount;
        }

        if (startPoint < 1) {
            startPoint = 1;
        }
*/
        var interval = getInterval(pagenumber,pagecount);
        // loop thru visible pages and render buttons
        for (var page = startPoint; page <= endPoint; page++) {

        	if(page > 2 && page < interval.start){
				if(page == 3)
					$('<li style="border:0px;">...</li>').appendTo($pager);
				continue;
			}
			
			if(page > interval.end && page < pagecount - 1){
				if(page == pagecount - 2)
					$('<li style="border:0px;">...</li>').appendTo($pager);
				continue;
			}

            var currentButton = $('<li class="page-number">' + (page) + '</li>');
            if (pagenumber == 0)
            	pagenumber = 1;
            page == pagenumber ? currentButton.addClass('pgCurrent') : currentButton.click(function() {
            	buttonClickCallback(this.firstChild.data);    
            });
            

            currentButton.appendTo($pager);
        }

        // render in the next and last buttons before returning the whole rendered control back. append(renderButton('下一页', pagenumber, pagecount, buttonClickCallback))
        $pager.append(renderButton('下一页', pagenumber, pagecount, buttonClickCallback)).append(renderButton('末页', pagenumber, pagecount, buttonClickCallback));

        return $pager;
    }

    // renders and returns a 'specialized' button, ie 'next', 'previous' etc. rather than a page number button
    function renderButton(buttonLabel, pagenumber, pagecount, buttonClickCallback) {

        var $Button = $('<li class="pgNext">' + buttonLabel + '</li>');

        var destPage = 1;

        // work out destination page for required button type
        switch (buttonLabel) {
            case "首页":
                destPage = 1;
                break;
            case "上一页":
                destPage = pagenumber - 1;
                break;
            case "下一页":
                destPage = pagenumber + 1;
                break;
            case "末页":
                destPage = pagecount;
                break;
        }

        // disable and 'grey' out buttons if not needed.
        if (buttonLabel == "首页" || buttonLabel == "上一页") {
            pagenumber <= 1 ? $Button.addClass('') : $Button.click(function() { buttonClickCallback(destPage); });
        }
        else {
            pagenumber >= pagecount ? $Button.addClass('') : $Button.click(function() { buttonClickCallback(destPage); });
        }

        return $Button;
    }

    // pager defaults. hardly worth bothering with in this case but used as placeholder for expansion in the next version
    $.fn.pager.defaults = {
        pagenumber: 1,
        pagecount: 1
    };

})(jQuery);





