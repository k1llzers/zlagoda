import {useEffect, useState} from "react";
import {useAuth} from "../provider/authProvider";
import axios from "axios";
import * as React from "react";
import TableContainer from "@mui/material/TableContainer";
import Card from "@mui/material/Card";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import StyledTableRow from "../styledComponent/styledTableRow";
import StyledTableCell from "../styledComponent/styledTableCell";
import TableBody from "@mui/material/TableBody";
import TableRow from "@mui/material/TableRow";
import {
    Box,
    Button,
    Dialog,
    DialogContent,
    FormControl,
    IconButton,
    InputAdornment,
    Stack,
    Typography
} from "@mui/material";
import KeyboardArrowUpIcon from "@mui/icons-material/KeyboardArrowUp";
import KeyboardArrowDownIcon from "@mui/icons-material/KeyboardArrowDown";
import ModeEditIcon from "@mui/icons-material/ModeEdit";
import DeleteOutlineOutlinedIcon from "@mui/icons-material/DeleteOutlineOutlined";
import Collapse from "@mui/material/Collapse";
import PercentIcon from '@mui/icons-material/Percent';
import {Container} from "react-bootstrap";
import Alert from "@mui/material/Alert";
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import NumberInputBasic from "../styledComponent/StyledNumberInput"



const StoreProducts = () => {
    const [storeProducts, setStoreProducts] = useState([])
    const [products, setProducts] = useState([])
    const {role} = useAuth()
    const [errorMessage, setErrorMessage] = useState("");
    const [openForm, setOpenForm] = useState(false)
    const [updateRow, setUpdateRow] = useState(undefined)

    const fetchStoreProductsData = async () => {
        let response
        if (role === "MANAGER")
            response = await axios.get("http://localhost:8080/api/store-product/order-by/count")
        else
            response = await axios.get("http://localhost:8080/api/store-product/order-by/name")
        setStoreProducts(response.data)
    }

    const fetchProductsData = async () => {
        const response = await axios.get("http://localhost:8080/api/product/order-by/name")
        setProducts(response.data)
    };

    useEffect(() => {
        fetchStoreProductsData()
    }, [])

    function processStoreProducts(storeProduct) {
        return {
            id: storeProduct.id,
            productId: storeProduct.product.id,
            name: storeProduct.product.name,
            sellingPrice: storeProduct.sellingPrice,
            productsNumber: storeProduct.productsNumber,
            characteristics: storeProduct.product.characteristics,
            promotional: storeProduct.promotional
        }
    }

    const columns = [
        {field: 'upc', headerName: 'UPC'},
        {field: 'product', headerName: 'Product'},
        {field: 'sellingPrice', headerName: 'Price'},
        {field: 'productsNumber', headerName: 'Number'},
        {field: 'promotional', headerName: 'Promotional'}
    ];

    const rows = storeProducts.map((storeProduct) => processStoreProducts(storeProduct))

    const clear = e => {
        setErrorMessage("")
    };

    function handleOpenForm() {
        setOpenForm(true)
        fetchProductsData()
    }

    function handleUpdate(row) {
        setUpdateRow(row)
        handleOpenForm()
    }

    function handleClose(){
        setOpenForm(false)
        setUpdateRow(undefined)
    }

    const handleDelete = async (storeProductId) => {
        const response = await axios.delete("http://localhost:8080/api/store-product/" + storeProductId)
        if (response.data.error) {
            setErrorMessage("Can't delete store product that is present in at least one check")
            setTimeout(() => setErrorMessage(""), 3500)
        } else {
            if (response.data === true)
                setStoreProducts(storeProducts.filter(storeProduct => storeProduct.id !== storeProductId));
        }
    }

    function handlePromotional() {

    }

    function StoreProductForm(props) {
        const {onClose, open, row} = props;
        const [sellingPrice, setSellingPrice] = useState(row ? row.sellingPrice : 0)
        const [productsNumber, setProductsNumber] = useState(row ? row.productsNumber : 0)
        const [product, setProduct] = useState(row ? row.productId : "")
        const [disableAdd, setDisableAdd] = useState(true)


        const handleAdd = async () => {
            handleClose()
            let response;
            if(!row) {
                response = await axios.post("http://localhost:8080/api/store-product", {
                    productId: +product,
                    sellingPrice: sellingPrice,
                    productsNumber: productsNumber,
                    promotional: false
                })
            } else {
                response = await axios.put("http://localhost:8080/api/store-product", {
                    id: row.id,
                    sellingPrice: sellingPrice,
                    productsNumber: productsNumber,
                })
            }
            if (response.data.error) {
                setErrorMessage(response.data.error)
                setTimeout(() => setErrorMessage(""), 3500)
            } else {
                fetchStoreProductsData()
            }
        }


        return (
            <Dialog onClose={onClose} open={open}>
                <DialogContent>
                    <FormControl fullWidth>
                        <NumberInputBasic
                            aria-label="Number of Products"
                            placeholder="Number of Products"
                            value={productsNumber}
                            min={0}
                            onChange={setProductsNumber}
                        />
                        <NumberInputBasic
                            aria-label="Selling Price"
                            placeholder="Selling price"
                            value={sellingPrice}
                            onChange={setSellingPrice}
                            min={1}
                            showAdornment
                        />
                    </FormControl>
                </DialogContent>
            </Dialog>
        )
    }

    function Row(props) {
        const {row} = props;
        const [open, setOpen] = useState(false);

        return (
            <React.Fragment>
                <TableRow sx={{'& > *': {borderBottom: 'unset'}}}>
                    <StyledTableCell>
                        <IconButton
                            aria-label="expand row"
                            size="small"
                            onClick={() => setOpen(!open)}>
                            {open ? <KeyboardArrowUpIcon/> : <KeyboardArrowDownIcon/>}
                        </IconButton>
                    </StyledTableCell>
                    <StyledTableCell component="th" scope="row " align="center">{row.id}</StyledTableCell>
                    <StyledTableCell component="th" scope="row " align="center">{row.name}</StyledTableCell>
                    <StyledTableCell align="center">{row.sellingPrice}</StyledTableCell>
                    <StyledTableCell align="center">{row.productsNumber}</StyledTableCell>
                    <StyledTableCell align="center">
                        <IconButton
                            size="small"
                            onClick={() => handlePromotional()}>
                            {row.promotional ? <PercentIcon color="success"/> : <PercentIcon color="action"/>}
                        </IconButton>
                    </StyledTableCell>
                    {role === "MANAGER" && <StyledTableCell align="right">
                        <Button onClick={() => handleUpdate(row)}>
                            <ModeEditIcon color='action'/>
                        </Button>
                        <Button onClick={() => handleDelete(row.id)}>
                            <DeleteOutlineOutlinedIcon color="error"/>
                        </Button>
                    </StyledTableCell>}
                </TableRow>
                <TableRow>
                    <StyledTableCell style={{paddingBottom: 0, paddingTop: 0}} colSpan={6}>
                        <Collapse in={open} timeout="auto" unmountOnExit>
                            <Box sx={{margin: 1}}>
                                <Typography variant="span" gutterBottom component="div" style={{fontSize: 18}}>
                                    Characteristics
                                </Typography>
                                {row.characteristics}
                            </Box>
                        </Collapse>
                    </StyledTableCell>
                </TableRow>
            </React.Fragment>
        );
    }

    function StoreProductsTable() {
        return (
            <React.Fragment>
                <TableContainer component={Card} sx={{ maxWidth: 900, margin: '30px auto' }}>
                    <Table aria-label="collapsible table">
                        <TableHead>
                            <StyledTableRow>
                                <StyledTableCell/>
                                {columns.map((column) => (
                                    <StyledTableCell align="center" key={column.field} sx={{width:"15%"}}>
                                        {column.headerName}
                                    </StyledTableCell>
                                ))}
                                <StyledTableCell sx={{width:"30%"}} />
                            </StyledTableRow>
                        </TableHead>
                        <TableBody>
                            {rows.map((row) => (
                                <Row key={row.name} row={row} />
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </React.Fragment>
        )
    }

    return (
        <Container style={{ marginTop: '50px'}}>
            <StoreProductForm
                open={openForm}
                onClose={handleClose}
                row={updateRow}
            />
            <Box sx={{maxWidth: 900, margin: '0 auto'}}>
                {errorMessage && <Alert style={{width: '40%', fontSize: '15px', position: 'fixed', right: '30%', top: '5%'}} severity="error" onClose={clear}>{errorMessage}</Alert>}
                <StoreProductsTable/>
            </Box>
        </Container>
    );

}

export default StoreProducts