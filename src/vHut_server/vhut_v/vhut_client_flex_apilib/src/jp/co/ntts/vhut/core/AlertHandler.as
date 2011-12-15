/**
 *  Copyright (c) 2007 - 2009 Adobe
 *  All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the "Software"),
 *  to deal in the Software without restriction, including without limitation
 *  the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included
 *  in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 *  THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *  IN THE SOFTWARE.
 */
package jp.co.ntts.vhut.core
{
    import jp.co.ntts.vhut.log.VhutLog;
    import jp.co.ntts.vhut.log.VhutLogLogger;

    import mx.controls.Alert;
    import mx.logging.ILogger;
    import mx.logging.Log;
    import mx.rpc.events.FaultEvent;

    public class AlertHandler
    {
		protected var logger:VhutLogLogger = VhutLog.getLogger("jp.co.ntts.vhut.core");

		[MessageHandler]
		public function showAlert(event:AlertEvent):void
		{
			var text:String = event.text;
			if (text == "" || text == null)
			{
				text = "An error occurred.";
			}

			Alert.show(text);
		}

		[GlobalRemoteObjectFaultHandler]
		public function logError(event:FaultEvent):void
		{
//			logger..error("A server error occurred. event={0}", event.toString());
		}
    }
}