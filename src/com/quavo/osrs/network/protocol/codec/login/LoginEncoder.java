/**
 * MIT License
 *
 * Copyright (c) 2017 Quavo
 * Copyright (c) 2017 Jordan Abraham
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.quavo.osrs.network.protocol.codec.login;

import com.quavo.osrs.network.handler.outbound.LoginResponse;
import com.quavo.osrs.network.protocol.ClientMessage;
import com.quavo.osrs.network.protocol.codec.login.world.WorldLoginDecoder;
import com.quavo.osrs.network.protocol.codec.login.world.WorldLoginEncoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author _jordan <citellumrsps@gmail.com>
 */
public final class LoginEncoder extends MessageToByteEncoder<LoginResponse> {

	/**
	 * Constructs a new object.
	 */
	public LoginEncoder() {
		super(LoginResponse.class);
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, LoginResponse msg, ByteBuf out) throws Exception {
		ClientMessage message = msg.getMessage();
		ChannelPipeline pipeline = ctx.pipeline();

		if (message != ClientMessage.SUCCESSFUL) {
			// dont write the id for successful.
			out.writeByte(message.getId());
		} else {
			pipeline.addAfter("login.decoder", "world.encoder", new WorldLoginEncoder());
			pipeline.replace("login.decoder", "world.decoder", new WorldLoginDecoder(msg.getType()));
		}

		pipeline.remove(this);
	}

}
