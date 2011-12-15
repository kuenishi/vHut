/*
 * Copyright 2011 NTT Software Corporation.
 * All Rights Reserved.
 */
package jp.co.ntts.vhut.core
{
	import flash.utils.ByteArray;
	import flash.utils.Endian;

	/**
	 * ビット配列
	 *
	 * <p>
	 * <b>Author :</b> NTT Software Corporation.
	 * <b>Version :</b> 1.0.0
	 * </p>
	 *
	 * @langversion 3.0
	 * @playerversion Flash 10.1
	 *
	 * @internal
	 * $Date: 2011-11-28 19:50:40 +0900 (月, 28 11 2011) $
	 * $Revision: 949 $
	 * $Author: NTT Software Corporation. $
	 */
	public class BitArray
	{
		private static const LAST_BIT:uint=0x80000000;
		private static const FIRST_BIT:uint=1;

		/**
		 * バイト配列からビット配列を生成します.
		 * @param value バイト配列
		 * @return ビット配列
		 */
		public static function newFromByteArray(value:ByteArray):BitArray
		{
			var byteArray:ByteArray = new ByteArray();
			byteArray.writeBytes(value);
			byteArray.endian = Endian.LITTLE_ENDIAN;
			var bitArray:BitArray = new BitArray(0);
			var tmpByteLength:int = byteArray.length % 4;
			if(tmpByteLength > 0)
			{
				for(var i:int=0; i<4-tmpByteLength; i++)
				{
					byteArray.writeByte(0);
				}
			}
			byteArray.position=0;
			bitArray._bitFields = new Array();
			while (byteArray.bytesAvailable > 3) {
				bitArray._bitFields.push(byteArray.readUnsignedInt());
				bitArray._bitLength += 32;
			}
			bitArray.length = value.length*8;
			return bitArray;
		}

		/**
		 *  Constructs a BitArray of the given length
		 */
		public function BitArray(length:uint=0)
		{
			var fields:uint=uint(Math.ceil(Number(length) / 32.0)) || 1;

			_bitFields=new Array(fields);

			for (var f:uint=0; f < fields; f++)
				_bitFields[f]=uint(0);

			_bitLength=length;
		}

		private var _bitFields:Array;

		//-----------------------------------------------------------------------------------------
		//
		//  Properties
		//
		//-----------------------------------------------------------------------------------------

		//-----------------------------------------------------------------------------------------
		//  length
		//-----------------------------------------------------------------------------------------

		private var _bitLength:uint;

		/**
		 *  The length of the array.
		 *
		 *  Will add zeros to the end if the new length is longer than the previous.
		 */
		public function get length():uint
		{
			return _bitLength;
		}

		public function set length(s:uint):void
		{
			if (s == _bitLength)
				return;

			if (s < _bitLength)
			{
				while (s < _bitLength)
					remove(_bitLength - 1);
			}

			var fields:uint=uint(Math.ceil(Number(s) / 32.0)) || 1;

			while (fields > _bitFields.length)
			{
				_bitFields.push(uint(0));
			}

			_bitLength=s;
		}

		//-----------------------------------------------------------------------------------------
		//  all
		//-----------------------------------------------------------------------------------------

		/**
		 *  Returns true if all bits are 1.
		 */
		public function get all():Boolean
		{
			for (var i:uint=0; i < _bitFields.length - 1; i++)
			{
				if (uint(_bitFields[i]) != uint.MAX_VALUE)
					return false;
			}

			var lastBitIndex:uint=(_bitLength & 31);

			var mask:uint=~(uint.MAX_VALUE & ((1 << lastBitIndex) - 1));

			// if lastBitIndex is 3, mask = (11111111 11111111 11111111 11111000)
			// if lastBitIndex is 6, mask = (11111111 11111111 11111111 11000000)
			// etc

			var word:uint=_bitFields[_bitFields.length - 1];
			var result:uint=word | mask;

			return result == uint.MAX_VALUE;
		}

		//-----------------------------------------------------------------------------------------
		//  any
		//-----------------------------------------------------------------------------------------

		/**
		 *  Returns true if any bit is 1.
		 */
		public function get any():Boolean
		{
			for (var i:uint=0; i < _bitFields.length; i++)
			{
				if (_bitFields[i] > 0)
					return true;
			}
			return false;
		}

		//-----------------------------------------------------------------------------------------
		//
		//  Public methods
		//
		//-----------------------------------------------------------------------------------------

		/**
		 *  Adds a bit to the end of the array.
		 */
		public function push(value:Boolean):void
		{
			var lastFieldIndex:uint=_bitFields.length - 1; // == (_bitLength / 32)-1;
			var lastBitIndex:uint=(_bitLength - 1) & 31; // == (_bitLength-1) % 32;

			if (_bitLength == 0)
			{
				lastFieldIndex=0;
				lastBitIndex=0;
			}
			else if (lastBitIndex == 31)
			{
				_bitFields.push(uint(0));
				lastFieldIndex++;
				lastBitIndex=0;
			}
			else
			{
				lastBitIndex++;
			}

			var mask:uint=(1 << lastBitIndex);
			var word:uint=_bitFields[lastFieldIndex];

			_bitFields[lastFieldIndex]^=(-uint(value) ^ word) & mask;

			//
			//  above is same as below, without branching (i.e. cooler).
			//
			// if (value)
			//     _bitFields[lastFieldIndex] = word | mask;
			// else
			//     _bitFields[lastFieldIndex] = word & ~mask;
			//

			_bitLength++;
		}

		private function pushWord(value:uint):void
		{
			_bitFields.push(value);
			_bitLength += 32;
		}

		/**
		 *  Removes the bit at the given position.
		 */
		public function remove(index:uint):void
		{
			var fieldIndex:uint=uint(index / 32);
			var lastFieldIndex:uint=_bitFields.length - 1;

			if (fieldIndex > lastFieldIndex)
				throw new RangeError("index");

			var bitIndex:uint=index & 31;

			var word:uint;
			var next:uint;
			var shift:uint;
			var mask:uint;

			word=uint(_bitFields[fieldIndex]); //  1110 1011 1010 1101 1110 1011 1010 1101
			shift=(word >>> 1); // 0111 0101 1101 0110 1111 0101 1101 0110

			mask=(1 << bitIndex) - 1; // 0000 0000 0000 0000 0000 0000 0001 1111

			// shift the last part of the word one bit right
			_bitFields[fieldIndex]=shift ^ ((shift ^ word) & mask);

			//                                       0111 0101 1101 0110 1111 0101 1100 1101

			//
			//  above is same as below, with one op less.
			//
			// _bitFields[fieldIndex] = (word & mask) | (shift & ~mask);
			//

			while (fieldIndex < lastFieldIndex)
			{
				word=uint(_bitFields[fieldIndex]);
				next=uint(_bitFields[fieldIndex + 1])

				// copy first bit in next word to last bit in the current word
				var firstbit:uint=next & FIRST_BIT;
				//_bitFields[fieldIndex] ^= (-uint(firstbit>0) ^ word) & LAST_BIT;

				if (firstbit > 0)
					_bitFields[fieldIndex]=word | LAST_BIT;

				// shift the next word one bit
				_bitFields[fieldIndex + 1]=(next >>> 1);

				fieldIndex++;
			}

			_bitLength--;
		}

		/**
		 *  Returns the value at the given position.
		 */

		public function getAt(index:uint):Boolean
		{
			var bitIndex:uint=index & 31; // index % 32
			var fieldIndex:uint=uint(index / 32);
			var mask:uint=(1 << bitIndex);
			var word:uint=_bitFields[fieldIndex];
			return Boolean(uint(word & mask));
		}

		/**
		 *  Sets the value at the given position.
		 */
		public function setAt(index:uint, value:Boolean):void
		{
			var bitIndex:uint=index & 31; // index % 32
			var fieldIndex:uint=uint(index / 32);

			var mask:uint=(1 << bitIndex);
			var word:uint=_bitFields[fieldIndex];

			_bitFields[fieldIndex]^=(-uint(value) ^ word) & mask;
		}


		/**
		 * バイト配列に変換します.
		 * @return バイト配列
		 */
		public function toByteArray():ByteArray
		{
			var byteArray:ByteArray = new ByteArray();
			byteArray.endian = Endian.LITTLE_ENDIAN;
			for(var i:uint; i<_bitFields.length; i++)
			{
				byteArray.writeUnsignedInt(_bitFields[i]);
			}
			byteArray.position = 0;
			byteArray.endian = Endian.BIG_ENDIAN;
			return byteArray;
		}

		public function and(source:BitArray):BitArray
		{
			var result:BitArray = new BitArray(Math.max(length, source.length));
			var fieldLength:uint = Math.max(_bitFields.length, source._bitFields.length);
			for (var i:uint=0; i<fieldLength; i++)
			{
				if (i >= _bitFields.length)
				{
					result._bitFields[i] = source._bitFields[i];
				} else if (i >= source._bitFields.length) {
					result._bitFields[i] = _bitFields[i];
				} else {
					result._bitFields[i] = _bitFields[i] & source._bitFields[i];
				}
			}
			return result;
		}

		public function or(source:BitArray):BitArray
		{
			var result:BitArray = new BitArray(Math.max(length, source.length));
			var fieldLength:uint = Math.max(_bitFields.length, source._bitFields.length);
			for (var i:uint=0; i<fieldLength; i++)
			{
				if (i >= _bitFields.length)
				{
					result._bitFields[i] = source._bitFields[i];
				} else if (i >= source._bitFields.length) {
					result._bitFields[i] = _bitFields[i];
				} else {
					result._bitFields[i] = _bitFields[i] | source._bitFields[i];
				}
			}
			return result;
		}

		public function xor(source:BitArray):BitArray
		{
			var result:BitArray = new BitArray(Math.max(length, source.length));
			var fieldLength:uint = Math.max(_bitFields.length, source._bitFields.length);
			for (var i:uint=0; i<fieldLength; i++)
			{
				if (i >= _bitFields.length)
				{
					result._bitFields[i] = source._bitFields[i];
				} else if (i >= source._bitFields.length) {
					result._bitFields[i] = _bitFields[i];
				} else {
					result._bitFields[i] = _bitFields[i] ^ source._bitFields[i];
				}
			}
			return result;
		}

		public function not():BitArray
		{
			var result:BitArray = new BitArray(length);
			for (var i:uint=0; i<_bitFields.length; i++)
			{
				result._bitFields[i] = ~_bitFields[i];
			}
			return result;
		}
	}
}