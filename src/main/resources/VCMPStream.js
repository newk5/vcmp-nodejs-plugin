class VCMPStream {
    constructor(array = new ByteBuffer()) {
        this.bytes = 0;
        this.bb = new ByteBuffer(array);
        this.bb.order = ByteBuffer.LITTLE_ENDIAN;
        this.values = [];
    }

    readInt() {
        return this.bb.readInt();
    }

    writeInt(v) {
        this.values.push({type: "int", value: v});
        this.bytes += 4;
        return this;
    }

    readFloat() {
        return this.bb.readFloat();
    }

    writeFloat(v) {
        this.values.push({type: "float", value: v});
        this.bytes += 4;
        return this;
    }

    readByte() {
        return this.bb.readByte();
    }

    writeByte(v) {
        this.values.push({type: "byte", value: v});
        this.bytes += 1;
        return this;
    }

    readString() {
        const order = this.bb.order;
        this.bb.order = ByteBuffer.BIG_ENDIAN;
        const result = this.bb.readString(this.bb.readUnsignedShort());
        this.bb.order = order;
        return result;
    }

    writeString(v) {
        this.values.push({type: "string", value: v});
        this.bytes += Buffer.byteLength(v, "utf8");
        this.bytes += 2;
        return this;
    }

    send(player) {
        player.sendStream(this);
    }

    build() {
        this.bb = new ByteBuffer(this.bytes);
        this.bb.order = ByteBuffer.LITTLE_ENDIAN;

        this.values.forEach(v => {
            if (v.type == "byte") {
                this.bb.writeByte(parseInt(v.value));
            } else if (v.type == "float") {
                this.bb.writeFloat(parseFloat(v.value + ""));
            } else if (v.type == "int") {
                this.bb.writeInt(parseInt(v.value + ""));
            } else if (v.type == "string") {

                v.value = String(v.value);
                const order = this.bb.order;
                this.bb.order = ByteBuffer.BIG_ENDIAN;
                this.bb.writeUnsignedShort(v.value.length);
                this.bb.writeString(v.value);
                this.bb.order = order;


            }

        });
    }

}

